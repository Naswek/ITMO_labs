package org.example.exceptions;


import jakarta.ws.rs.core.Response;

public class InvalidInputException extends ApiException {

    public InvalidInputException(String message) {
        super(Response.Status.BAD_REQUEST, "INVALID_INPUT", message);
    }
}
