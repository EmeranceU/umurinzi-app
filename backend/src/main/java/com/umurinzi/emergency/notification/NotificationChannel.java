package com.umurinzi.emergency.notification;

/** SDD §1.6, §5.7 — EMAIL and CALL are future channels; the dispatcher shape already accommodates them. */
public enum NotificationChannel {
    PUSH,
    SMS,
    EMAIL,
    CALL
}
