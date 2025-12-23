package org.example.exceptions;

import jakarta.ws.rs.core.Response;

public class InvalidEmailException extends ApiException {
    public InvalidEmailException(String message) {
        super(Response.Status.BAD_REQUEST, "EMAIL_INVALID", message);
    }
}
