package com.umurinzi.emergency.security.jwt;

import com.umurinzi.emergency.security.UserPrincipal;
import com.umurinzi.emergency.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Authenticates every request carrying a valid {@code Authorization: Bearer <accessToken>}
 * header (SDD §5, all endpoints). Looks the user up fresh from the DB on every request
 * (rather than trusting the JWT's claims alone) so a suspended/deleted account or role
 * change is honored immediately, not just at token expiry (SDD §6).
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {
        extractBearerToken(request)
                .flatMap(jwtTokenProvider::parseAccessToken)
                .map(JwtTokenProvider::subjectAsUserId)
                .flatMap(this::loadActiveUser)
                .ifPresent(principal -> {
                    var authentication = new UsernamePasswordAuthenticationToken(
                            principal, null, principal.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });

        chain.doFilter(request, response);
    }

    private Optional<UserPrincipal> loadActiveUser(UUID userId) {
        return userRepository.findById(userId).map(UserPrincipal::new);
    }

    private Optional<String> extractBearerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return Optional.of(header.substring(7));
        }
        return Optional.empty();
    }
}
