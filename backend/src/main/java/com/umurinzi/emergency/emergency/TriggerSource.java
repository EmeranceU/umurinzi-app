package com.umurinzi.emergency.emergency;

/**
 * SDD §2.2/§5.5 — {@code MANUAL_APP} covers the in-app SOS button and has no paired
 * hardware, which is why {@code EmergencyEvent.device} is nullable.
 */
public enum TriggerSource {
    BLE_BUTTON,
    MANUAL_APP
}
