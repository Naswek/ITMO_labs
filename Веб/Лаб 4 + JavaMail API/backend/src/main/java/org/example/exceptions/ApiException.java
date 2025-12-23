package org.example.exceptions;

import jakarta.ws.rs.core.Response;

public abstract class ApiException extends RuntimeException {
    private final Response.Status status;
    private final String code;

    protected ApiException(Response.Status status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public Response.Status getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}