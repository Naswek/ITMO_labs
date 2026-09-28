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
        if (labWork.getVersion() != null) {
            throw new IllegalArgumentException("Версия новой лабораторной работы назначается сервером");
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
        if (labWork.getVersion() == null || labWork.getVersion() < 0) {
            throw new IllegalArgumentException("Для изменения укажите актуальную версию лабораторной работы");
        }
    }

    public void validate(LabWork labWork) {
        if (labWork == null) {
            throw new IllegalArgumentException("Лабораторная работа не задана");
        }
        requireName(labWork.getName(), "Название работы");
        if (labWork.getName().length() > 255) {
            throw new IllegalArgumentException("Название работы не может быть длиннее 255 символов");
        }
        validateCoordinates(labWork.getCoordinates());

        if (labWork.getDescription() == null || labWork.getDescription().length() > 3429) {
            throw new IllegalArgumentException("Описание обязательно и не может быть длиннее 3429 символов");
        }
        if (labWork.getDifficulty() == null) {
            throw new IllegalArgumentException("Укажите сложность работы");
        }
        if (!Double.isFinite(labWork.getMinimalPoint()) || labWork.getMinimalPoint() <= 0) {
            throw new IllegalArgumentException("Минимальный балл должен быть конечным числом больше нуля");
        }
        if (labWork.getAveragePoint() == null || labWork.getAveragePoint() <= 0) {
            throw new IllegalArgumentException("Средний балл должен быть целым числом больше нуля");
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
            throw new IllegalArgumentException("Укажите координаты работы");
        }
        if (!Float.isFinite(coordinates.getX()) || coordinates.getX() <= -523) {
            throw new IllegalArgumentException("Координата X должна быть конечным числом больше -523");
        }
        if (coordinates.getY() == null || coordinates.getY() <= -643) {
            throw new IllegalArgumentException("Координата Y должна быть целым числом больше -643");
        }
    }

    private static void requireName(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " не может быть пустым");
        }
    }
}
