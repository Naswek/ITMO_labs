package org.example.utils.cdi.validators;

import jakarta.enterprise.context.ApplicationScoped;
import org.example.entity.Location;
import org.example.entity.Person;

@ApplicationScoped
public class PersonValidator {

    public void validateForCreate(Person value) {
        validate(value);
        if (value.getId() != null) {
            throw new IllegalArgumentException("ID автора генерирует база данных");
        }
    }

    public void validateForUpdate(long id, Person value) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID автора должен быть больше нуля");
        }
        validate(value);
        if (value.getId() != null && value.getId() != id) {
            throw new IllegalArgumentException("ID автора в теле и адресе не совпадают");
        }
    }

    public void validate(Person value) {
        if (value == null || value.getName() == null || value.getName().isBlank()) {
            throw new IllegalArgumentException("Имя автора не может быть пустым");
        }
        if (value.getName().length() > 255) {
            throw new IllegalArgumentException("Имя автора не может быть длиннее 255 символов");
        }
        if (value.getEyeColor() == null || value.getNationality() == null || value.getBirthday() == null) {
            throw new IllegalArgumentException("Укажите цвет глаз, гражданство и дату рождения автора");
        }
        if (value.getWeight() != null && value.getWeight() <= 0) {
            throw new IllegalArgumentException("Вес должен быть больше нуля");
        }
        Location location = value.getLocation();
        if (location == null) {
            throw new IllegalArgumentException("Укажите местоположение автора");
        }
        if (location.getX() == null) {
            throw new IllegalArgumentException("Укажите координату X местоположения");
        }
        if (location.getY() == null || !Double.isFinite(location.getY())) {
            throw new IllegalArgumentException("Координата Y местоположения должна быть конечным числом");
        }
        if (location.getName() == null || location.getName().length() > 500) {
            throw new IllegalArgumentException("Название местоположения обязательно и не может быть длиннее 500 символов");
        }
    }
}
