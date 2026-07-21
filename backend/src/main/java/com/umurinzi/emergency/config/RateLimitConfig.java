package com.umurinzi.emergency.config;

/**
 * Placeholder for the Bucket4j + Redis rate-limiting setup (SDD §6): a distributed
 * {@code ProxyManager} plus the per-endpoint bucket definitions (stricter on
 * {@code /auth/login}, {@code /auth/register}, {@code /auth/forgot-password}; generous
 * on {@code /emergencies/*}/locations}).
 *
 * <p>Deliberately not wired yet — which endpoints get which limits is a Phase 1/3
 * decision made alongside the modules that own those endpoints, not something to guess
 * at during scaffolding. The {@code bucket4j-redis} dependency is already on the
 * classpath (see {@code pom.xml}) so this class has what it needs when that phase
 * starts; a {@code RateLimitingFilter} (SDD §3 {@code security} package) will consume
 * the buckets defined here.
 */
public final class RateLimitConfig {

    private RateLimitConfig() {}
}
