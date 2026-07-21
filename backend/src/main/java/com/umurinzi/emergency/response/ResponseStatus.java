package com.umurinzi.emergency.response;

/**
 * SDD §1.4d — progression: {@code NOTIFIED -> VIEWED -> ACCEPTED -> ON_MY_WAY}.
 * Renamed from {@code RESPONDING} to {@code ACCEPTED} in v1.3 to match the Helper
 * action vocabulary (Accept / On My Way / Call Police / Mark Safe).
 */
public enum ResponseStatus {
    NOTIFIED,
    VIEWED,
    ACCEPTED,
    ON_MY_WAY
}
