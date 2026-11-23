package org.example.utils.cdi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;
import org.example.entity.Coordinates;
import org.example.entity.Discipline;
import org.example.entity.LabWork;
import org.example.enums.Difficulty;
import org.example.utils.cdi.validators.DisciplineValidator;
import org.example.utils.cdi.validators.LabWorkValidator;
import org.example.utils.cdi.validators.PersonValidator;
import org.example.utils.repository.LabWorkRepository;
import org.junit.jupiter.api.Test;

class ImportBatchServiceTest {

    @Test
    void invalidLaterRowPreventsAnyInsert() throws Exception {
        AtomicInteger inserts = new AtomicInteger();
        ImportBatchService service = new ImportBatchService();
        inject(service, "workValidator", new LabWorkValidator());
        inject(service, "disciplineValidator", new DisciplineValidator());
        inject(service, "personValidator", new PersonValidator());
        inject(service, "works", new LabWorkRepository() {
            @Override public LabWork create(LabWork value) { inserts.incrementAndGet(); return value; }
        });

        LabWork invalid = work();
        invalid.setAveragePoint(0);
        assertThrows(IllegalArgumentException.class, () -> service.importWorks(1, new LabWork[] { work(), invalid }));
        assertEquals(0, inserts.get());
    }

    @Test
    void duplicateNamesInsideFilePreventAnyInsert() throws Exception {
        AtomicInteger inserts = new AtomicInteger();
        ImportBatchService service = new ImportBatchService();
        inject(service, "workValidator", new LabWorkValidator());
        inject(service, "disciplineValidator", new DisciplineValidator());
        inject(service, "personValidator", new PersonValidator());
        inject(service, "uniqueness", new DomainUniquenessPolicy() {
            @Override public void lockUntilTransactionEnds() { }
            @Override public void requireWorkNameAvailable(String name, Integer exceptId) { }
            @Override public void requireCoordinatesAvailable(Coordinates value, Integer exceptId) { }
        });
        inject(service, "works", new LabWorkRepository() {
            @Override public LabWork create(LabWork value) { inserts.incrementAndGet(); return value; }
        });

        assertThrows(DuplicateLabWorkNameException.class,
                () -> service.importWorks(1, new LabWork[] { work(), work() }));
        assertEquals(0, inserts.get());
    }

    @Test
    void duplicateCoordinatesInsideFilePreventAnyInsert() throws Exception {
        AtomicInteger inserts = new AtomicInteger();
        ImportBatchService service = new ImportBatchService();
        inject(service, "workValidator", new LabWorkValidator());
        inject(service, "disciplineValidator", new DisciplineValidator());
        inject(service, "personValidator", new PersonValidator());
        inject(service, "uniqueness", permissivePolicy());
        inject(service, "works", new LabWorkRepository() {
            @Override public LabWork create(LabWork value) { inserts.incrementAndGet(); return value; }
        });

        LabWork second = work();
        second.setName("Other");
        assertThrows(DuplicateValueException.class,
                () -> service.importWorks(1, new LabWork[] { work(), second }));
        assertEquals(0, inserts.get());
    }

    @Test
    void duplicateDisciplineNamesInsideFilePreventAnyInsert() throws Exception {
        AtomicInteger inserts = new AtomicInteger();
        ImportBatchService service = new ImportBatchService();
        inject(service, "workValidator", new LabWorkValidator());
        inject(service, "disciplineValidator", new DisciplineValidator());
        inject(service, "personValidator", new PersonValidator());
        inject(service, "uniqueness", permissivePolicy());
        inject(service, "works", new LabWorkRepository() {
            @Override public LabWork create(LabWork value) { inserts.incrementAndGet(); return value; }
        });

        LabWork first = work();
        first.setDiscipline(discipline("Database"));
        LabWork second = work();
        second.setName("Other");
        second.getCoordinates().setY(3);
        second.setDiscipline(discipline(" database "));
        assertThrows(DuplicateValueException.class,
                () -> service.importWorks(1, new LabWork[] { first, second }));
        assertEquals(0, inserts.get());
    }

    private static DomainUniquenessPolicy permissivePolicy() {
        return new DomainUniquenessPolicy() {
            @Override public void lockUntilTransactionEnds() { }
            @Override public void requireWorkNameAvailable(String name, Integer exceptId) { }
            @Override public void requireCoordinatesAvailable(Coordinates value, Integer exceptId) { }
            @Override public void requireDisciplineNameAvailable(String name, Long exceptId) { }
        };
    }

    private static Discipline discipline(String name) {
        Discipline value = new Discipline();
        value.setName(name);
        value.setPracticeHours(1L);
        value.setSelfStudyHours(2L);
        return value;
    }

    private static LabWork work() {
        Coordinates coordinates = new Coordinates();
        coordinates.setX(1);
        coordinates.setY(2);
        LabWork value = new LabWork();
        value.setName("Test");
        value.setCoordinates(coordinates);
        value.setDescription("");
        value.setDifficulty(Difficulty.EASY);
        value.setMinimalPoint(1);
        value.setAveragePoint(2);
        return value;
    }

    private static void inject(Object target, String name, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
