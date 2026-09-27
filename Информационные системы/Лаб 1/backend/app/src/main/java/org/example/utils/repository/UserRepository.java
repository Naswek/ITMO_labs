package org.example.utils.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import org.example.entity.UserEntity;

@ApplicationScoped
public class UserRepository {

    @PersistenceContext(unitName = "labworks")
    private EntityManager entityManager;

    @Transactional
    public UserEntity create(UserEntity user) {
        Objects.requireNonNull(user, "user");
        if (user.getId() != null) {
            throw new IllegalArgumentException("ID нового пользователя должен генерироваться базой данных");
        }

        entityManager.persist(user);
        return user;
    }

    public UserEntity findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID должен быть больше нуля");
        }
        return entityManager.find(UserEntity.class, id);
    }

    public UserEntity findByLogin(String login) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Логин не может быть пустым");
        }

        return entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.login = :login", UserEntity.class)
                .setParameter("login", login)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public List<UserEntity> findAll() {
        return entityManager.createQuery("SELECT u FROM UserEntity u ORDER BY u.id", UserEntity.class)
                .getResultList();
    }

    @Transactional
    public UserEntity update(UserEntity changes) {
        Objects.requireNonNull(changes, "changes");
        if (changes.getId() == null || changes.getId() <= 0) {
            throw new IllegalArgumentException("ID должен быть больше нуля");
        }

        UserEntity current = entityManager.find(UserEntity.class, changes.getId());
        if (current == null) {
            throw new EntityNotFoundException("Пользователь с ID " + changes.getId() + " не найден");
        }

        current.setLogin(changes.getLogin());
        current.setPasswordHash(changes.getPasswordHash());
        return current;
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID должен быть больше нуля");
        }

        UserEntity user = entityManager.find(UserEntity.class, id);
        if (user == null) {
            return false;
        }

        entityManager.remove(user);
        return true;
    }
}
