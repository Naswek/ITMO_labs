package org.example.utils.cdi.validators;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.entity.Discipline;

@ApplicationScoped
public class DisciplineValidator {

    public void validateForCreate(Discipline value) {
        validate(value);
        if (value.getId() != null) {
            throw new IllegalArgumentException("ID дисциплины генерирует база данных");
        }
    }

    public void validateForUpdate(long id, Discipline value) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID дисциплины должен быть больше нуля");
        }
        validate(value);
        if (value.getId() != null && value.getId() != id) {
            throw new IllegalArgumentException("ID дисциплины в теле и адресе не совпадают");
        }
    }

    public void validate(Discipline value) {
        if (value == null || value.getName() == null || value.getName().isBlank()) {
            throw new IllegalArgumentException("Название дисциплины не может быть пустым");
        }
        if (value.getName().length() > 255) {
            throw new IllegalArgumentException("Название дисциплины не может быть длиннее 255 символов");
        }
        if (value.getPracticeHours() == null) {
            throw new IllegalArgumentException("practiceHours обязателен");
        }
    }
}
