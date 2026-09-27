package org.example.utils.cdi.validators;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserValidator {

    public String normalizeLogin(String login) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Логин не может быть пустым");
        }
        String normalized = login.trim();
        if (normalized.length() > 100) {
            throw new IllegalArgumentException("Логин не может быть длиннее 100 символов");
        }
        return normalized;
    }

    public void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }
    }
}
