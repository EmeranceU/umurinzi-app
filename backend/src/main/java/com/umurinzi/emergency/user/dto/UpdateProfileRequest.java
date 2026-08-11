package com.umurinzi.emergency.user.dto;

/**
 * SDD §5.2 PATCH /users/me. Every field is optional — only non-null fields are
 * applied, so a client can update just one field (e.g. only `alertMode`) without
 * resending the whole profile.
 */
public record UpdateProfileRequest(
        String fullName,
        String phoneNumber,
        String profilePhotoUrl,
        String medicalNotes,
        String preferredLanguage,
        String alertMode,
        Boolean silenceOtherHelpersOnAccept) {}
