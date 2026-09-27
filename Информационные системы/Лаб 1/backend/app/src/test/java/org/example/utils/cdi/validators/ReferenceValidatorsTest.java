package org.example.utils.cdi.validators;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import org.example.entity.Discipline;
import org.example.entity.Location;
import org.example.entity.Person;
import org.example.enums.Color;
import org.example.enums.Country;
import org.junit.jupiter.api.Test;

class ReferenceValidatorsTest {

    private final DisciplineValidator disciplines = new DisciplineValidator();
    private final PersonValidator people = new PersonValidator();

    @Test
    void validatesDisciplineBeforeCreateAndUpdate() {
        Discipline value = new Discipline();
        value.setName("Математика");
        value.setPracticeHours(0L);
        assertDoesNotThrow(() -> disciplines.validateForCreate(value));

        value.setId(4L);
        assertThrows(IllegalArgumentException.class, () -> disciplines.validateForCreate(value));
        assertDoesNotThrow(() -> disciplines.validateForUpdate(4, value));
        assertThrows(IllegalArgumentException.class, () -> disciplines.validateForUpdate(5, value));
    }

    @Test
    void validatesAuthorAndNestedLocationBeforeSaving() {
        Person value = new Person();
        value.setName("Анна");
        value.setEyeColor(Color.GREEN);
        value.setNationality(Country.JAPAN);
        value.setBirthday(new Date());
        Location location = new Location();
        location.setX(1L);
        location.setY(2.0);
        location.setName("");
        value.setLocation(location);
        assertDoesNotThrow(() -> people.validateForCreate(value));

        location.setY(null);
        assertThrows(IllegalArgumentException.class, () -> people.validateForCreate(value));
    }
}
