package main.java.org.example.utils.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import org.example.entity.LabWork;

@ApplicationScoped
public class LabWorkRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public LabWork create(LabWork labWork) {
        Objects.requireNonNull(labWork, "labWork");
        if (labWork.getId() != 0) {
            throw new IllegalArgumentException("ID новой лабораторной работы должен генерироваться базой данных");
        }

        entityManager.persist(labWork);
        return labWork;
    }

    public LabWork findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID должен быть больше нуля");
        }
        return entityManager.find(LabWork.class, id);
    }

    public List<LabWork> findAll() {
        return entityManager.createQuery("SELECT l FROM LabWork l ORDER BY l.id", LabWork.class)
                .getResultList();
    }

    public List<LabWork> findPage(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Номер страницы не может быть отрицательным, размер должен быть больше нуля");
        }
        if (page > Integer.MAX_VALUE / size) {
            throw new IllegalArgumentException("Слишком большой номер страницы");
        }

        int offset = page * size;
        return entityManager.createQuery("SELECT l FROM LabWork l ORDER BY l.id", LabWork.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    public long count() {
        return entityManager.createQuery("SELECT COUNT(l) FROM LabWork l", Long.class)
                .getSingleResult();
    }

    @Transactional
    public LabWork update(LabWork changes) {
        Objects.requireNonNull(changes, "changes");
        if (changes.getId() <= 0) {
            throw new IllegalArgumentException("ID должен быть больше нуля");
        }

        LabWork current = entityManager.find(LabWork.class, changes.getId());
        if (current == null) {
            throw new EntityNotFoundException("Лабораторная работа с ID " + changes.getId() + " не найдена");
        }

        current.setName(changes.getName());
        current.setCoordinates(changes.getCoordinates());
        current.setDescription(changes.getDescription());
        current.setDifficulty(changes.getDifficulty());
        current.setDiscipline(changes.getDiscipline());
        current.setMinimalPoint(changes.getMinimalPoint());
        current.setAveragePoint(changes.getAveragePoint());
        current.setAuthor(changes.getAuthor());
        return current;
    }

    @Transactional
    public boolean deleteById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID должен быть больше нуля");
        }

        LabWork labWork = entityManager.find(LabWork.class, id);
        if (labWork == null) {
            return false;
        }

        entityManager.remove(labWork);
        return true;
    }
}
