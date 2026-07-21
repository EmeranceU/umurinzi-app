package com.umurinzi.emergency.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base type for exceptions {@link GlobalExceptionHandler} translates into the
 * standard {@link com.umurinzi.emergency.common.dto.ApiResponse} error envelope.
 */
@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorCode code;

    public ApiException(HttpStatus status, ErrorCode code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }
}
