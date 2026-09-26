package org.example.utils.cdi;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import org.example.entity.LabWork;
import org.example.utils.repository.LabWorkRepository;

@ApplicationScoped
public class LabWorkService {

    @Inject
    private LabWorkRepository repository;

    @Inject
    private LabWorkValidator validator;

    @Transactional
    public LabWork add(LabWork labWork) {
        validator.validate(labWork);
        return repository.create(labWork);
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

    public long count() {
        return repository.count();
    }

    @Transactional
    public LabWork update(int id, LabWork changes) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID должен быть больше нуля");
        }
        validator.validate(changes);
        if (changes.getId() != 0 && changes.getId() != id) {
            throw new IllegalArgumentException("ID в данных не совпадает с ID лабораторной работы");
        }

        changes.setId(id);
        return repository.update(changes);
    }

    @Transactional
    public void delete(int id) {
        if (!repository.deleteById(id)) {
            throw new EntityNotFoundException("Лабораторная работа с ID " + id + " не найдена");
        }
    }
}
