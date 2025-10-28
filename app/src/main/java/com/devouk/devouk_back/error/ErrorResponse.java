package com.devouk.devouk_back.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private final LocalDateTime timestamp;
    private final String path;
    private final String message;
    private final String correlationId;
    private final List<FieldErrorDetail> errors;

    public static ErrorResponse of(
            String path,
            String message,
            String correlationId,
            List<FieldErrorDetail> errors) {
        return new ErrorResponse(LocalDateTime.now(), path, message, correlationId, errors);
    }

    @Getter
    @AllArgsConstructor
    public static class FieldErrorDetail {
        private final String field;
        private final String code;
        private final String errorMessage;
    }
}
