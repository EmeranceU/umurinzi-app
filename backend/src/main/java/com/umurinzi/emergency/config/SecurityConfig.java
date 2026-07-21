package com.umurinzi.emergency.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Baseline security chain for a stateless JWT API (SDD §6).
 *
 * <p><b>Phase 0 scope only:</b> this wires the shape of the chain — stateless sessions,
 * CSRF off (not needed for a token-based API), and the public/permitted paths every
 * later module will rely on (Swagger, actuator health, and the {@code /auth/**} and
 * {@code /public/**} endpoints once they exist). It does <b>not</b> yet register a JWT
 * authentication filter or any role-based ({@code @PreAuthorize}) rules — those land in
 * Phase 1 alongside the {@code auth}/{@code security.jwt} packages. Until then every
 * non-permitted endpoint requires a Spring Security principal that nothing in this
 * codebase can yet produce, which is the intended, honest state for a scaffold: routes
 * are not accidentally left wide open.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_PATHS = {
        "/actuator/health",
        "/actuator/health/**",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/auth/**",
        "/public/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_PATHS).permitAll()
                        .anyRequest().authenticated());
        // TODO (Phase 1): register JwtAuthenticationFilter before
        // UsernamePasswordAuthenticationFilter, and layer @PreAuthorize/@RequireRole
        // checks per SDD §6 RBAC.
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
