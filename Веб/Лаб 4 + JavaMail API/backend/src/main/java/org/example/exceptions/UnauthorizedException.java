package org.example.exceptions;

import jakarta.ws.rs.core.Response;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String message) {
        super(Response.Status.UNAUTHORIZED, "UNAUTHORIZED", message);
    }
}
