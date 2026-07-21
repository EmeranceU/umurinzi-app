/**
 * JWT authentication, {@code UserPrincipal}, and RBAC support (SDD §3, §6).
 *
 * <p>Phase 1 work: {@code JwtTokenProvider}, {@code JwtAuthenticationFilter},
 * {@code UserPrincipal}, {@code CustomUserDetailsService}, {@code RateLimitingFilter},
 * and the {@code @RequireRole}/{@code @CurrentUser} annotations in {@code
 * security.annotation}. Not implemented in Phase 0 scaffolding.
 */
package com.umurinzi.emergency.security;
