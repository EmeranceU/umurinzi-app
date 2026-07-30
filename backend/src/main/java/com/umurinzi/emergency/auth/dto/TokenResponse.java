package com.umurinzi.emergency.auth.dto;

import com.umurinzi.emergency.user.dto.UserProfileResponse;

public record TokenResponse(
        String accessToken, String refreshToken, long expiresIn, UserProfileResponse user) {}
