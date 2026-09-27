package org.example.utils.cdi.validators;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.entity.Coordinates;
import org.example.entity.LabWork;

@ApplicationScoped
public class LabWorkValidator {

    public void validateForCreate(LabWork labWork) {
        validate(labWork);
        if (labWork.getId() != 0) {
            throw new IllegalArgumentException("ID новой лабораторной работы должен генерироваться базой данных");
        }
    }

    public void validateForUpdate(int id, LabWork labWork) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID должен быть больше нуля");
        }
        validate(labWork);
        if (labWork.getId() != 0 && labWork.getId() != id) {
            throw new IllegalArgumentException("ID в данных не совпадает с ID лабораторной работы");
        }
    }

    public void validate(LabWork labWork) {
        if (labWork == null) {
            throw new IllegalArgumentException("Лабораторная работа не задана");
        }
        requireName(labWork.getName(), "name");
        if (labWork.getName().length() > 255) {
            throw new IllegalArgumentException("name не может быть длиннее 255 символов");
        }
        validateCoordinates(labWork.getCoordinates());

        if (labWork.getDescription() == null || labWork.getDescription().length() > 3429) {
            throw new IllegalArgumentException("description обязателен и не может быть длиннее 3429 символов");
        }
        if (labWork.getDifficulty() == null) {
            throw new IllegalArgumentException("difficulty обязателен");
        }
        if (!(labWork.getMinimalPoint() > 0)) {
            throw new IllegalArgumentException("minimalPoint должен быть больше нуля");
        }
        if (labWork.getAveragePoint() == null || labWork.getAveragePoint() <= 0) {
            throw new IllegalArgumentException("averagePoint должен быть больше нуля");
        }

        if (labWork.getDiscipline() != null) {
            if (labWork.getDiscipline().getId() == null || labWork.getDiscipline().getId() <= 0) {
                throw new IllegalArgumentException("Сначала создайте дисциплину и передайте её ID");
            }
        }
        if (labWork.getAuthor() != null) {
            if (labWork.getAuthor().getId() == null || labWork.getAuthor().getId() <= 0) {
                throw new IllegalArgumentException("Сначала создайте автора и передайте его ID");
            }
        }
    }

    private static void validateCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("coordinates обязателен");
        }
        if (!(coordinates.getX() > -523)) {
            throw new IllegalArgumentException("coordinates.x должен быть больше -523");
        }
        if (coordinates.getY() == null || coordinates.getY() <= -643) {
            throw new IllegalArgumentException("coordinates.y должен быть больше -643");
        }
    }

    private static void requireName(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " не может быть пустым");
        }
    }
}
