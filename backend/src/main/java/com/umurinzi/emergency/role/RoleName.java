package com.umurinzi.emergency.role;

/**
 * Type-safe reference to the row names seeded by {@code V2__seed_roles.sql}. See the
 * Design note in SDD §1.1: {@code EMERGENCY_CONTACT} is kept for onboarding UX and
 * {@code ADMIN} is a true global privilege, but Helper Dashboard access is
 * relationship-derived (§1.1), not gated on this enum.
 */
public enum RoleName {
    USER,
    EMERGENCY_CONTACT,
    ADMIN
}
