package org.example.utils.cdi;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.example.entity.LabWork;
import org.example.enums.Difficulty;
import org.example.utils.cdi.validators.LabWorkValidator;
import org.example.utils.repository.LabWorkRepository;

@ApplicationScoped
public class LabWorkService {

    @Inject
    private LabWorkRepository repository;

    @Inject
    private LabWorkValidator validator;

    @Inject
    private ReferenceService references;

    @Inject
    private Event<CollectionChanged> changes;

    @Transactional
    public LabWork add(LabWork labWork) {
        validator.validateForCreate(labWork);
        resolveReferences(labWork);
        LabWork created = repository.create(labWork);
        changes.fire(new CollectionChanged());
        return created;
    }

    public LabWork info(int id) {
        LabWork labWork = repository.findById(id);
        if (labWork == null) {
            throw new EntityNotFoundException("Лабораторная работа с ID " + id + " не найдена");
        }
        return labWork;
    }

    public List<LabWork> list(int page, int size) {
        return repository.findPage(page, size);
    }

    public Page list(int page, int size, String filterField, String filterValue, String sortField, String direction) {
        if (page < 0 || size < 1 || size > 100 || (long) page * size > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Страница должна быть неотрицательной, размер — от 1 до 100");
        }
        List<LabWork> values = new ArrayList<>(repository.findAll());
        if (filterValue != null && !filterValue.isBlank()) {
            String field = requireStringField(filterField);
            String needle = filterValue.toLowerCase(Locale.ROOT);
            values.removeIf(value -> !stringValue(value, field).toLowerCase(Locale.ROOT).contains(needle));
        }
        if (sortField != null && !sortField.isBlank()) {
            String field = requireStringField(sortField);
            Comparator<LabWork> comparator = Comparator.comparing(
                    value -> stringValue(value, field), String.CASE_INSENSITIVE_ORDER);
            if ("desc".equalsIgnoreCase(direction)) {
                comparator = comparator.reversed();
            } else if (direction != null && !direction.isBlank() && !"asc".equalsIgnoreCase(direction)) {
                throw new IllegalArgumentException("Направление сортировки: asc или desc");
            }
            values.sort(comparator.thenComparingInt(LabWork::getId));
        }
        int from = Math.min(page * size, values.size());
        int to = Math.min(from + size, values.size());
        return new Page(values.subList(from, to), values.size(), page, size);
    }

    public long count() {
        return repository.count();
    }

    @Transactional
    public LabWork update(int id, LabWork changes) {
        validator.validateForUpdate(id, changes);
        changes.setId(id);
        resolveReferences(changes);
        LabWork updated = repository.update(changes);
        this.changes.fire(new CollectionChanged());
        return updated;
    }

    @Transactional
    public void delete(int id, Long expectedVersion) {
        if (expectedVersion == null || expectedVersion < 0) {
            throw new IllegalArgumentException("Для удаления укажите актуальную версию лабораторной работы");
        }
        if (!repository.deleteById(id, expectedVersion)) {
            throw new EntityNotFoundException("Лабораторная работа с ID " + id + " не найдена");
        }
        changes.fire(new CollectionChanged());
    }

    public Optional<LabWork> maximumDifficulty() {
        return repository.findAll().stream().max(Comparator.comparingInt(value -> value.getDifficulty().ordinal()));
    }

    public Map<Difficulty, Long> countByDifficulty() {
        Map<Difficulty, Long> counts = new EnumMap<>(Difficulty.class);
        for (Difficulty difficulty : Difficulty.values()) {
            counts.put(difficulty, 0L);
        }
        repository.findAll().forEach(value -> counts.merge(value.getDifficulty(), 1L, Long::sum));
        return counts;
    }

    public List<LabWork> nameContains(String substring) {
        if (substring == null || substring.isEmpty()) {
            throw new IllegalArgumentException("Укажите подстроку имени");
        }
        return repository.findAll().stream().filter(value -> value.getName().contains(substring)).toList();
    }

    @Transactional
    public LabWork shiftDifficulty(int id, int steps, boolean increase) {
        if (steps < 1) {
            throw new IllegalArgumentException("Число шагов должно быть больше нуля");
        }
        LabWork value = info(id);
        Difficulty[] levels = Difficulty.values();
        long next = (long) value.getDifficulty().ordinal() + (increase ? steps : -(long) steps);
        if (next < 0 || next >= levels.length) {
            throw new IllegalArgumentException("Сложность выйдет за границы доступных значений");
        }
        value.setDifficulty(levels[(int) next]);
        changes.fire(new CollectionChanged());
        return value;
    }

    private void resolveReferences(LabWork value) {
        if (value.getDiscipline() != null) {
            value.setDiscipline(references.discipline(value.getDiscipline().getId()));
        }
        if (value.getAuthor() != null) {
            value.setAuthor(references.person(value.getAuthor().getId()));
        }
    }

    private static String requireStringField(String field) {
        if (field == null) {
            throw new IllegalArgumentException("Укажите строковое поле для фильтрации");
        }
        switch (field) {
            case "name", "description", "difficulty", "discipline.name", "author.name",
                    "author.eyeColor", "author.hairColor", "author.nationality", "author.location.name" -> { return field; }
            default -> throw new IllegalArgumentException("Неизвестное строковое поле: " + field);
        }
    }

    private static String stringValue(LabWork value, String field) {
        return switch (field) {
            case "name" -> value.getName();
            case "description" -> value.getDescription();
            case "difficulty" -> value.getDifficulty().name();
            case "discipline.name" -> value.getDiscipline() == null ? "" : value.getDiscipline().getName();
            case "author.name" -> value.getAuthor() == null ? "" : value.getAuthor().getName();
            case "author.eyeColor" -> value.getAuthor() == null ? "" : value.getAuthor().getEyeColor().name();
            case "author.hairColor" -> value.getAuthor() == null || value.getAuthor().getHairColor() == null
                    ? "" : value.getAuthor().getHairColor().name();
            case "author.nationality" -> value.getAuthor() == null ? "" : value.getAuthor().getNationality().name();
            case "author.location.name" -> value.getAuthor() == null ? "" : value.getAuthor().getLocation().getName();
            default -> "";
        };
    }

    public record Page(List<LabWork> items, long total, int page, int size) {
    }
}
