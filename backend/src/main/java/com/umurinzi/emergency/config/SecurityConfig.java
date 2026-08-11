package com.umurinzi.emergency.config;

import com.umurinzi.emergency.security.jwt.JwtAuthenticationFilter;
import com.umurinzi.emergency.security.jwt.JwtTokenProvider;
import com.umurinzi.emergency.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Stateless JWT security chain (SDD §6). {@code /auth/**} and {@code /public/**} stay
 * open; every other endpoint requires a valid access token, authenticated by {@link
 * JwtAuthenticationFilter} (Phase 1). Role-based ({@code @PreAuthorize}/{@code
 * @RequireRole}) rules land module-by-module as each module's endpoints do, per SDD §6
 * RBAC — this chain only establishes *who* the caller is, not what they're allowed to
 * do once authenticated.
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
        "/public/**",
        "/uploads/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter)
            throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_PATHS).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        return new JwtAuthenticationFilter(jwtTokenProvider, userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
