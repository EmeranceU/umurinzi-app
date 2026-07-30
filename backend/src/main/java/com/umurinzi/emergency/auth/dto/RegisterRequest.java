package com.umurinzi.emergency.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank String fullName,
        @NotBlank @Email String email,
        @NotBlank String phoneNumber,
        @NotBlank @Size(min = 8, message = "password must be at least 8 characters") String password,
        String preferredLanguage) {}
