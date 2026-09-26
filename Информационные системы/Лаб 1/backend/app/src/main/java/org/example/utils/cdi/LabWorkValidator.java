package org.example.utils.cdi;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.entity.Coordinates;
import org.example.entity.Discipline;
import org.example.entity.LabWork;
import org.example.entity.Location;
import org.example.entity.Person;

@ApplicationScoped
public class LabWorkValidator {

    public void validate(LabWork labWork) {
        if (labWork == null) {
            throw new IllegalArgumentException("Лабораторная работа не задана");
        }
        requireName(labWork.getName(), "name");
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
            validateDiscipline(labWork.getDiscipline());
        }
        if (labWork.getAuthor() != null) {
            validatePerson(labWork.getAuthor());
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

    private static void validateDiscipline(Discipline discipline) {
        requireName(discipline.getName(), "discipline.name");
        if (discipline.getPracticeHours() == null) {
            throw new IllegalArgumentException("discipline.practiceHours обязателен");
        }
    }

    private static void validatePerson(Person person) {
        requireName(person.getName(), "author.name");
        if (person.getEyeColor() == null) {
            throw new IllegalArgumentException("author.eyeColor обязателен");
        }
        if (person.getBirthday() == null) {
            throw new IllegalArgumentException("author.birthday обязателен");
        }
        if (person.getWeight() != null && person.getWeight() <= 0) {
            throw new IllegalArgumentException("author.weight должен быть больше нуля");
        }
        if (person.getNationality() == null) {
            throw new IllegalArgumentException("author.nationality обязателен");
        }
        validateLocation(person.getLocation());
    }

    private static void validateLocation(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("author.location обязателен");
        }
        if (location.getX() == null || location.getY() == null) {
            throw new IllegalArgumentException("author.location.x и author.location.y обязательны");
        }
        if (location.getName() == null || location.getName().length() > 500) {
            throw new IllegalArgumentException("author.location.name обязателен и не может быть длиннее 500 символов");
        }
    }

    private static void requireName(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " не может быть пустым");
        }
    }
}
