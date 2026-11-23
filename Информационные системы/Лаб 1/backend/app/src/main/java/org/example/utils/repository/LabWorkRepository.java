package org.example.utils.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.example.dto.LabWorkPage;
import org.example.entity.LabWork;

@ApplicationScoped
public class LabWorkRepository {

    private static final String PAGE_FROM = " FROM lab_work l"
            + " LEFT JOIN discipline d ON d.id = l.discipline_id"
            + " LEFT JOIN person p ON p.id = l.author_id";

    @PersistenceContext(unitName = "labworks")
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

    public LabWorkPage findPage(String filterField, String filterValue, String sortField,
            String direction, int page, int size) {
        boolean filtered = filterField != null;
        String where = filtered ? " WHERE LOWER(COALESCE(" + stringColumn(filterField)
                + ", '')) LIKE ?1 ESCAPE '!'" : "";
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*)" + PAGE_FROM + where);
        if (filtered) countQuery.setParameter(1, likePattern(filterValue));
        long total = ((Number) countQuery.getSingleResult()).longValue();
        int actualPage = (int) Math.min(page, total == 0 ? 0 : (total - 1) / size);

        String order = sortField == null ? "l.id" : "LOWER(COALESCE(" + stringColumn(sortField)
                + ", '')) " + ("desc".equals(direction) ? "DESC" : "ASC") + ", l.id";
        long offset = (long) actualPage * size;
        Query pageQuery = entityManager.createNativeQuery("SELECT l.*" + PAGE_FROM + where
                + " ORDER BY " + order + " LIMIT " + size + " OFFSET " + offset, LabWork.class);
        if (filtered) pageQuery.setParameter(1, likePattern(filterValue));
        @SuppressWarnings("unchecked")
        List<LabWork> items = pageQuery.getResultList();
        return new LabWorkPage(items, total, actualPage, size);
    }

    private static String stringColumn(String field) {
        return switch (field) {
            case "name" -> "l.name";
            case "description" -> "l.description";
            case "difficulty" -> "l.difficulty";
            case "discipline.name" -> "d.name";
            case "author.name" -> "p.name";
            case "author.eyeColor" -> "p.eye_color";
            case "author.hairColor" -> "p.hair_color";
            case "author.nationality" -> "p.nationality";
            case "author.location.name" -> "p.location_name";
            default -> throw new IllegalArgumentException("Неизвестное строковое поле: " + field);
        };
    }

    private static String likePattern(String value) {
        return "%" + value.toLowerCase(Locale.ROOT).replace("!", "!!")
                .replace("%", "!%").replace("_", "!_") + "%";
    }

    public List<String> namesExceptId(Integer exceptId) {
        String jpql = "SELECT l.name FROM LabWork l";
        if (exceptId != null) jpql += " WHERE l.id <> :exceptId";
        var query = entityManager.createQuery(jpql, String.class);
        if (exceptId != null) query.setParameter("exceptId", exceptId);
        return query.getResultList();
    }

    public boolean existsByCoordinatesExceptId(float x, Integer y, Integer exceptId) {
        String jpql = "SELECT COUNT(l) FROM LabWork l WHERE l.coordinates.x = :x"
                + " AND l.coordinates.y = :y";
        if (exceptId != null) jpql += " AND l.id <> :exceptId";
        var query = entityManager.createQuery(jpql, Long.class)
                .setParameter("x", x).setParameter("y", y);
        if (exceptId != null) query.setParameter("exceptId", exceptId);
        return query.getSingleResult() > 0;
    }

    public boolean existsByAuthorDisciplineExceptId(Long authorId, Long disciplineId, Integer exceptId) {
        String jpql = "SELECT COUNT(l) FROM LabWork l WHERE l.author.id = :authorId"
                + " AND l.discipline.id = :disciplineId";
        if (exceptId != null) jpql += " AND l.id <> :exceptId";
        var query = entityManager.createQuery(jpql, Long.class)
                .setParameter("authorId", authorId).setParameter("disciplineId", disciplineId);
        if (exceptId != null) query.setParameter("exceptId", exceptId);
        return query.getSingleResult() > 0;
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
        if (!Objects.equals(current.getVersion(), changes.getVersion())) {
            throw new StaleObjectException("Работа уже изменена другим пользователем. Загрузите актуальные данные перед сохранением");
        }

        current.setName(changes.getName());
        current.setCoordinates(changes.getCoordinates());
        current.setDescription(changes.getDescription());
        current.setDifficulty(changes.getDifficulty());
        current.setDiscipline(changes.getDiscipline());
        current.setMinimalPoint(changes.getMinimalPoint());
        current.setAveragePoint(changes.getAveragePoint());
        current.setAuthor(changes.getAuthor());
        entityManager.flush();
        return current;
    }

    @Transactional
    public boolean deleteById(int id, long expectedVersion) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID должен быть больше нуля");
        }

        LabWork labWork = entityManager.find(LabWork.class, id);
        if (labWork == null) {
            return false;
        }
        if (!Objects.equals(labWork.getVersion(), expectedVersion)) {
            throw new StaleObjectException("Работа уже изменена другим пользователем. Загрузите актуальные данные перед удалением");
        }

        entityManager.remove(labWork);
        entityManager.flush();
        return true;
    }
}
