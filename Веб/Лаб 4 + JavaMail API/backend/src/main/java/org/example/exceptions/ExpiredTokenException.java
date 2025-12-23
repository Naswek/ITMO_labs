package org.example.exceptions;

import jakarta.ws.rs.core.Response;

public class ExpiredTokenException extends ApiException {
    public ExpiredTokenException(String message) {
        super(Response.Status.UNAUTHORIZED, "EXPIRED_TOKEN", message);
    }
}
