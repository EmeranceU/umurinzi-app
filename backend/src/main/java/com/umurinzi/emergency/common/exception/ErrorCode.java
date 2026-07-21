package com.umurinzi.emergency.common.exception;

/**
 * Machine-readable error codes returned in {@link com.umurinzi.emergency.common.dto.ApiResponse.ApiError#code()}
 * (SDD §5.12). Generic codes only for now — each module adds its own
 * domain-specific codes (e.g. {@code EMERGENCY_INVALID_TRANSITION}, {@code HELPER_NOT_LINKED})
 * as that module's business logic lands.
 */
public enum ErrorCode {
    VALIDATION_ERROR,
    NOT_FOUND,
    UNAUTHORIZED,
    FORBIDDEN,
    CONFLICT,
    RATE_LIMITED,
    INTERNAL_ERROR
}
