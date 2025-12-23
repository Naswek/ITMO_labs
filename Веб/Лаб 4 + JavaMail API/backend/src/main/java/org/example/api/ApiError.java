package org.example.api;

import java.time.Instant;

public record ApiError(String error, String code, Instant timestamp) {
    public static ApiError of(String error, String code) {
        return new ApiError(error, code, Instant.now());
    }
}
