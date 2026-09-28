package org.example.utils.repository;

public class StaleObjectException extends RuntimeException {

    public StaleObjectException(String message) {
        super(message);
    }
}
