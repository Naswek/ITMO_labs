package org.example.utils.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.example.entity.Discipline;
import org.example.entity.LabWork;
import org.example.entity.Person;

@ApplicationScoped
public class ReferenceRepository {

    @PersistenceContext(unitName = "labworks")
    private EntityManager entityManager;

    public List<Discipline> disciplines() {
        return entityManager.createQuery("SELECT d FROM Discipline d ORDER BY d.id", Discipline.class).getResultList();
    }

    public List<Person> people() {
        return entityManager.createQuery("SELECT p FROM Person p ORDER BY p.id", Person.class).getResultList();
    }

    public Discipline discipline(long id) {
        Discipline value = entityManager.find(Discipline.class, id);
        if (value == null) {
            throw new EntityNotFoundException("Дисциплина с ID " + id + " не найдена");
        }
        return value;
    }

    public Person person(long id) {
        Person value = entityManager.find(Person.class, id);
        if (value == null) {
            throw new EntityNotFoundException("Автор с ID " + id + " не найден");
        }
        return value;
    }

    public Discipline create(Discipline value) {
        entityManager.persist(value);
        return value;
    }

    public Person create(Person value) {
        entityManager.persist(value);
        return value;
    }

    public List<LabWork> using(Discipline value) {
        return entityManager.createQuery("SELECT l FROM LabWork l WHERE l.discipline = :value", LabWork.class)
                .setParameter("value", value).getResultList();
    }

    public List<LabWork> using(Person value) {
        return entityManager.createQuery("SELECT l FROM LabWork l WHERE l.author = :value", LabWork.class)
                .setParameter("value", value).getResultList();
    }

    public void remove(Discipline value) {
        entityManager.remove(value);
    }

    public void remove(Person value) {
        entityManager.remove(value);
    }
}
