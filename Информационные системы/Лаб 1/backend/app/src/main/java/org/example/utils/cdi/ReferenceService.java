package org.example.utils.cdi;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import org.example.entity.Discipline;
import org.example.entity.LabWork;
import org.example.entity.Person;
import org.example.utils.cdi.validators.DisciplineValidator;
import org.example.utils.cdi.validators.PersonValidator;
import org.example.utils.repository.ReferenceRepository;

@ApplicationScoped
public class ReferenceService {

    @Inject
    private ReferenceRepository repository;

    @Inject
    private DisciplineValidator disciplineValidator;

    @Inject
    private PersonValidator personValidator;

    @Inject
    private Event<CollectionChanged> changes;

    public List<Discipline> disciplines() {
        return repository.disciplines();
    }

    public List<Person> people() {
        return repository.people();
    }

    public Discipline discipline(long id) {
        return repository.discipline(id);
    }

    public Person person(long id) {
        return repository.person(id);
    }

    @Transactional
    public Discipline add(Discipline value) {
        disciplineValidator.validateForCreate(value);
        Discipline created = repository.create(value);
        changes.fire(new CollectionChanged());
        return created;
    }

    @Transactional
    public Person add(Person value) {
        personValidator.validateForCreate(value);
        Person created = repository.create(value);
        changes.fire(new CollectionChanged());
        return created;
    }

    @Transactional
    public Discipline updateDiscipline(long id, Discipline changes) {
        disciplineValidator.validateForUpdate(id, changes);
        Discipline current = repository.discipline(id);
        current.setName(changes.getName());
        current.setPracticeHours(changes.getPracticeHours());
        current.setSelfStudyHours(changes.getSelfStudyHours());
        current.setLabsCount(changes.getLabsCount());
        this.changes.fire(new CollectionChanged());
        return current;
    }

    @Transactional
    public Person updatePerson(long id, Person changes) {
        personValidator.validateForUpdate(id, changes);
        Person current = repository.person(id);
        current.setName(changes.getName());
        current.setEyeColor(changes.getEyeColor());
        current.setHairColor(changes.getHairColor());
        current.setLocation(changes.getLocation());
        current.setBirthday(changes.getBirthday());
        current.setWeight(changes.getWeight());
        current.setNationality(changes.getNationality());
        this.changes.fire(new CollectionChanged());
        return current;
    }

    @Transactional
    public void deleteDiscipline(long id, Long replacementId) {
        Discipline target = repository.discipline(id);
        List<LabWork> linked = repository.using(target);
        if (!linked.isEmpty()) {
            if (replacementId == null || replacementId == id) {
                throw new IllegalArgumentException("Укажите ID другой дисциплины для связанных лабораторных работ");
            }
            Discipline replacement = repository.discipline(replacementId);
            linked.forEach(work -> work.setDiscipline(replacement));
        }
        repository.remove(target);
        changes.fire(new CollectionChanged());
    }

    @Transactional
    public void deletePerson(long id, Long replacementId) {
        Person target = repository.person(id);
        List<LabWork> linked = repository.using(target);
        if (!linked.isEmpty()) {
            if (replacementId == null || replacementId == id) {
                throw new IllegalArgumentException("Укажите ID другого автора для связанных лабораторных работ");
            }
            Person replacement = repository.person(replacementId);
            linked.forEach(work -> work.setAuthor(replacement));
        }
        repository.remove(target);
        changes.fire(new CollectionChanged());
    }

}
