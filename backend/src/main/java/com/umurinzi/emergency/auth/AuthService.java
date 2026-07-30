package com.umurinzi.emergency.auth;

import com.umurinzi.emergency.auth.dto.LoginRequest;
import com.umurinzi.emergency.auth.dto.RefreshRequest;
import com.umurinzi.emergency.auth.dto.RegisterRequest;
import com.umurinzi.emergency.auth.dto.TokenResponse;
import com.umurinzi.emergency.common.exception.ApiException;
import com.umurinzi.emergency.common.exception.ErrorCode;
import com.umurinzi.emergency.role.Role;
import com.umurinzi.emergency.role.RoleName;
import com.umurinzi.emergency.role.RoleRepository;
import com.umurinzi.emergency.security.jwt.JwtTokenProvider;
import com.umurinzi.emergency.user.AlertMode;
import com.umurinzi.emergency.user.User;
import com.umurinzi.emergency.user.UserRepository;
import com.umurinzi.emergency.user.UserStatus;
import com.umurinzi.emergency.user.dto.UserProfileResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Register / login / refresh / logout (SDD §5.1). Deliberately checks the password
 * manually against {@link PasswordEncoder} rather than wiring a full {@code
 * AuthenticationManager} — this is the whole of what Phase 1 auth needs, and a
 * generic authentication abstraction earns its keep once there's more than one
 * credential type to support.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public TokenResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ApiException(HttpStatus.CONFLICT, ErrorCode.CONFLICT, "Email already registered");
        }
        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new ApiException(HttpStatus.CONFLICT, ErrorCode.CONFLICT, "Phone number already registered");
        }

        Role userRole = roleRepository
                .findByName(RoleName.USER.name())
                .orElseThrow(() -> new IllegalStateException("USER role missing — was V2__seed_roles.sql applied?"));

        User user = new User();
        user.setRole(userRole);
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setPreferredLanguage(request.preferredLanguage());
        user.setAlertMode(AlertMode.SILENT);
        user.setSilenceOtherHelpersOnAccept(false);
        user.setStatus(UserStatus.ACTIVE);
        user.setEmailVerified(false);
        // saveAndFlush (not save): @CreationTimestamp is only populated by Hibernate at
        // actual INSERT time, which a plain save() can defer past this method's return —
        // issueTokens() below builds the response DTO from this same in-memory instance.
        user = userRepository.saveAndFlush(user);

        return issueTokens(user);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository
                .findByEmail(request.email())
                .filter(candidate -> passwordEncoder.matches(request.password(), candidate.getPasswordHash()))
                .filter(candidate -> candidate.getStatus() == UserStatus.ACTIVE)
                .orElseThrow(() ->
                        new ApiException(HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED, "Invalid email or password"));

        return issueTokens(user);
    }

    @Transactional
    public TokenResponse refresh(RefreshRequest request) {
        var claims = jwtTokenProvider
                .parseRefreshToken(request.refreshToken())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED, "Invalid refresh token"));

        RefreshToken stored = refreshTokenRepository
                .findByTokenHash(sha256(request.refreshToken()))
                .filter(token -> !token.isRevoked())
                .filter(token -> token.getExpiresAt().isAfter(Instant.now()))
                .orElseThrow(() -> new ApiException(
                        HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED, "Refresh token expired or revoked"));

        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        User user = userRepository
                .findById(JwtTokenProvider.subjectAsUserId(claims))
                .filter(candidate -> candidate.getStatus() == UserStatus.ACTIVE)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED, "Account no longer active"));

        return issueTokens(user);
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.findByTokenHash(sha256(refreshToken)).ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });
    }

    private TokenResponse issueTokens(User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getRole().getName());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());

        RefreshToken entity = new RefreshToken();
        entity.setUser(user);
        entity.setTokenHash(sha256(refreshToken));
        entity.setRevoked(false);
        entity.setIssuedAt(Instant.now());
        entity.setExpiresAt(jwtTokenProvider.getRefreshTokenExpiry());
        refreshTokenRepository.save(entity);

        return new TokenResponse(
                accessToken, refreshToken, jwtTokenProvider.getAccessTokenTtlSeconds(), UserProfileResponse.from(user));
    }

    private static String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
