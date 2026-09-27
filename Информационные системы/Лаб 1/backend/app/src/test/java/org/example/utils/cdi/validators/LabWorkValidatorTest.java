package org.example.utils.cdi.validators;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.example.entity.Coordinates;
import org.example.entity.Discipline;
import org.example.entity.LabWork;
import org.example.entity.Person;
import org.example.enums.Difficulty;
import org.junit.jupiter.api.Test;

class LabWorkValidatorTest {

    private final LabWorkValidator validator = new LabWorkValidator();

    @Test
    void acceptsBoundaryAdjacentValuesAndEmptyDescription() {
        LabWork value = valid();
        value.getCoordinates().setX(-522.999f);
        value.getCoordinates().setY(-642);
        value.setDescription("");
        assertDoesNotThrow(() -> validator.validate(value));
    }

    @Test
    void rejectsInvalidCoordinatesAndPoints() {
        LabWork invalidX = valid();
        invalidX.getCoordinates().setX(-523);
        assertThrows(IllegalArgumentException.class, () -> validator.validate(invalidX));

        LabWork invalidY = valid();
        invalidY.getCoordinates().setY(-643);
        assertThrows(IllegalArgumentException.class, () -> validator.validate(invalidY));

        LabWork invalidPoint = valid();
        invalidPoint.setMinimalPoint(0);
        assertThrows(IllegalArgumentException.class, () -> validator.validate(invalidPoint));
    }

    @Test
    void rejectsDescriptionOverLimit() {
        LabWork value = valid();
        value.setDescription("a".repeat(3430));
        assertThrows(IllegalArgumentException.class, () -> validator.validate(value));
    }

    @Test
    void acceptsExistingRelationsByIdWithoutRequiringTheirFullFields() {
        LabWork value = valid();
        Discipline discipline = new Discipline();
        discipline.setId(3L);
        Person author = new Person();
        author.setId(7L);
        value.setDiscipline(discipline);
        value.setAuthor(author);

        assertDoesNotThrow(() -> validator.validateForCreate(value));
    }

    @Test
    void rejectsRelationWithoutExistingIdBeforeSaving() {
        LabWork value = valid();
        value.setDiscipline(new Discipline());

        assertThrows(IllegalArgumentException.class, () -> validator.validateForCreate(value));
    }

    private static LabWork valid() {
        Coordinates coordinates = new Coordinates();
        coordinates.setX(1);
        coordinates.setY(1);
        LabWork value = new LabWork();
        value.setName("Test");
        value.setCoordinates(coordinates);
        value.setDescription("Test");
        value.setDifficulty(Difficulty.EASY);
        value.setMinimalPoint(1);
        value.setAveragePoint(1);
        return value;
    }
}
