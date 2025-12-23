package org.example.utils;

import org.example.exceptions.InvalidEmailException;

public class Email {

    private final String value;

    public Email(String value) {
        if (!isValid(value)) {
            throw new InvalidEmailException("Введенная почта невалидна. " + value);
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean isValid(String email) {
        return email != null && email.contains("@");
    }
}
