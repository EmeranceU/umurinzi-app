package com.umurinzi.emergency.notification;

/**
 * Server-computed from who the recipient is, never from the emergency owner's
 * {@code alertMode} (SDD §1.4c, §5.7). {@code HELPER_ALARM} forces FCM data-message,
 * high-priority delivery so the client's foreground service / full-screen intent /
 * Critical Alert controls presentation, not the OS notification tray.
 */
public enum NotificationPriority {
    NORMAL,
    HELPER_ALARM
}
