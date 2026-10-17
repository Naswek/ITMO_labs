package org.example.utils.cdi;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
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
}
