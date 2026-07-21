package com.umurinzi.emergency.user;

/**
 * Governs only what the owner's own device does when it triggers an emergency
 * (SDD §1.4c). Never affects what any Helper receives — Helper alerts are always
 * loud (§1.4d).
 */
public enum AlertMode {
    SILENT,
    AUDIBLE
}
