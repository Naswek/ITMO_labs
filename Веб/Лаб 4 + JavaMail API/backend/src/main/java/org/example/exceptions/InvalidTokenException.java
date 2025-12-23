package org.example.exceptions;

import jakarta.ws.rs.core.Response;

public class InvalidTokenException extends ApiException {
    public InvalidTokenException(String message) {
        super(Response.Status.UNAUTHORIZED, "INVALID_INPUT", message);
    }
}
