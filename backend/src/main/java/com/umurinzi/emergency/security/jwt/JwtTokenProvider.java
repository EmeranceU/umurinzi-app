package com.umurinzi.emergency.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Issues and validates the access/refresh JWT pair (SDD §6). Access tokens carry a
 * "role" claim for fast authorization checks; refresh tokens intentionally don't
 * (they're never used to authorize a request, only to mint a new access token via
 * {@code POST /auth/refresh}, at which point the DB is re-checked anyway — SDD §2.2).
 * Both carry a "type" claim so one can't be presented as the other.
 */
@Component
public class JwtTokenProvider {

    private static final String CLAIM_TYPE = "type";
    private static final String CLAIM_ROLE = "role";
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";

    private final SecretKey key;
    private final Duration accessTokenTtl;
    private final Duration refreshTokenTtl;

    public JwtTokenProvider(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-token-ttl-minutes}") long accessTokenTtlMinutes,
            @Value("${app.jwt.refresh-token-ttl-days}") long refreshTokenTtlDays) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenTtl = Duration.ofMinutes(accessTokenTtlMinutes);
        this.refreshTokenTtl = Duration.ofDays(refreshTokenTtlDays);
    }

    public String generateAccessToken(UUID userId, String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userId.toString())
                .claim(CLAIM_TYPE, TYPE_ACCESS)
                .claim(CLAIM_ROLE, role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(accessTokenTtl)))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(UUID userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userId.toString())
                .claim(CLAIM_TYPE, TYPE_REFRESH)
                .id(UUID.randomUUID().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(refreshTokenTtl)))
                .signWith(key)
                .compact();
    }

    public long getAccessTokenTtlSeconds() {
        return accessTokenTtl.toSeconds();
    }

    public Instant getRefreshTokenExpiry() {
        return Instant.now().plus(refreshTokenTtl);
    }

    /** Returns the parsed claims if {@code token} is a validly-signed, unexpired ACCESS token, otherwise empty. */
    public java.util.Optional<Claims> parseAccessToken(String token) {
        return parse(token, TYPE_ACCESS);
    }

    /** Returns the parsed claims if {@code token} is a validly-signed, unexpired REFRESH token, otherwise empty. */
    public java.util.Optional<Claims> parseRefreshToken(String token) {
        return parse(token, TYPE_REFRESH);
    }

    private java.util.Optional<Claims> parse(String token, String expectedType) {
        try {
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            if (!expectedType.equals(claims.get(CLAIM_TYPE, String.class))) {
                return java.util.Optional.empty();
            }
            return java.util.Optional.of(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return java.util.Optional.empty();
        }
    }

    public static UUID subjectAsUserId(Claims claims) {
        return UUID.fromString(claims.getSubject());
    }
}
