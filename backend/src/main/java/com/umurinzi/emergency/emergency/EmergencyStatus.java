package com.umurinzi.emergency.emergency;

/**
 * One-way state machine, enforced in the service layer, not just the DB (SDD §2.2):
 * {@code ACTIVE -> RESOLVED | FALSE_ALARM | CANCELLED}.
 */
public enum EmergencyStatus {
    ACTIVE,
    RESOLVED,
    FALSE_ALARM,
    CANCELLED
}
