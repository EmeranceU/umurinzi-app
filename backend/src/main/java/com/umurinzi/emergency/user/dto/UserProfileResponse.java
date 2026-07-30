package com.umurinzi.emergency.user.dto;

import com.umurinzi.emergency.user.User;
import java.time.Instant;
import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        String fullName,
        String email,
        String phoneNumber,
        String profilePhotoUrl,
        String medicalNotes,
        String preferredLanguage,
        String alertMode,
        boolean silenceOtherHelpersOnAccept,
        String role,
        String status,
        boolean emailVerified,
        Instant createdAt) {

    public static UserProfileResponse from(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getProfilePhotoUrl(),
                user.getMedicalNotes(),
                user.getPreferredLanguage(),
                user.getAlertMode().name(),
                user.isSilenceOtherHelpersOnAccept(),
                user.getRole().getName(),
                user.getStatus().name(),
                user.isEmailVerified(),
                user.getCreatedAt());
    }
}
