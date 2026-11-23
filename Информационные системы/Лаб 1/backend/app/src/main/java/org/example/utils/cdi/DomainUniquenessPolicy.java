package org.example.utils.cdi;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Synchronization;
import jakarta.transaction.TransactionSynchronizationRegistry;
import java.util.Locale;
import java.util.concurrent.Semaphore;
import org.example.entity.Coordinates;
import org.example.utils.repository.LabWorkRepository;
import org.example.utils.repository.ReferenceRepository;

/**
 * Checks application-level uniqueness rules while serializing competing writes
 * in this application instance. The permit is held until commit or rollback.
 */
@ApplicationScoped
public class DomainUniquenessPolicy {

    @Inject
    private LabWorkRepository works;

    @Inject
    private ReferenceRepository references;

    @Resource(lookup = "java:comp/TransactionSynchronizationRegistry")
    private TransactionSynchronizationRegistry transactions;

    private final Semaphore writes = new Semaphore(1, true);

    public void lockUntilTransactionEnds() {
        writes.acquireUninterruptibly();
        try {
            transactions.registerInterposedSynchronization(new Synchronization() {
                @Override public void beforeCompletion() { }
                @Override public void afterCompletion(int status) { writes.release(); }
            });
        } catch (RuntimeException error) {
            writes.release();
            throw error;
        }
    }

    public static String nameKey(String value) {
        return value.strip().toLowerCase(Locale.ROOT);
    }

    public static CoordinatesKey coordinatesKey(Coordinates value) {
        // PostgreSQL regards -0.0 and 0.0 as equal.
        float x = value.getX() == 0f ? 0f : value.getX();
        return new CoordinatesKey(x, value.getY());
    }

    public void requireWorkNameAvailable(String name, Integer exceptId) {
        String key = nameKey(name);
        if (works.namesExceptId(exceptId).stream().anyMatch(existing -> nameKey(existing).equals(key))) {
            throw new DuplicateLabWorkNameException("Работа с названием «" + name.strip() + "» уже существует");
        }
    }

    public void requireCoordinatesAvailable(Coordinates coordinates, Integer exceptId) {
        if (works.existsByCoordinatesExceptId(coordinates.getX(), coordinates.getY(), exceptId)) {
            throw new DuplicateValueException("Работа с координатами ("
                    + coordinates.getX() + ", " + coordinates.getY() + ") уже существует");
        }
    }

    public void requireDisciplineNameAvailable(String name, Long exceptId) {
        String key = nameKey(name);
        if (references.disciplineNamesExceptId(exceptId).stream()
                .anyMatch(existing -> nameKey(existing).equals(key))) {
            throw new DuplicateValueException("Дисциплина с названием «" + name.strip() + "» уже существует");
        }
    }

    public void requireAuthorDisciplineAvailable(Long authorId, Long disciplineId, Integer exceptId) {
        if (authorId != null && disciplineId != null
                && works.existsByAuthorDisciplineExceptId(authorId, disciplineId, exceptId)) {
            throw new DuplicateValueException(
                    "У автора уже есть лабораторная работа по этой дисциплине");
        }
    }

    public record CoordinatesKey(float x, Integer y) { }
}
