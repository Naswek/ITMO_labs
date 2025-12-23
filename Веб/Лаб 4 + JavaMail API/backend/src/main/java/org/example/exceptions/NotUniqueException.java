package org.example.exceptions;

import jakarta.ws.rs.core.Response;

public class NotUniqueException extends ApiException {
    public NotUniqueException(String message) {
        super(Response.Status.CONFLICT, "NOT_UNIQUE",  message);
    }
}
