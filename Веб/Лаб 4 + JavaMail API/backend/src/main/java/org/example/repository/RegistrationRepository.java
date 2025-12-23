package org.example.repository;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.entity.PendingRegistrationEntity;
import org.example.entity.UserEntity;
import org.example.exceptions.NotUniqueException;
import org.example.services.EmailService;

import java.time.LocalDateTime;

@Stateless
public class RegistrationRepository {

    @PersistenceContext(name = "default")
    private EntityManager entityManager;

    @Inject
    private EmailService emailService;

    public void save(PendingRegistrationEntity pendingRegistration) {
        if (isLoginExists(pendingRegistration.getLogin())) {
            throw new NotUniqueException("Такой логин уже существует");
        }

        if (isEmailExists(pendingRegistration.getEmail())) {
            throw new NotUniqueException("Такой email уже существует");
        }

        entityManager.persist(pendingRegistration);
    }

    public PendingRegistrationEntity findPendingByToken(String token) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<PendingRegistrationEntity> criteriaQuery = criteriaBuilder.createQuery(PendingRegistrationEntity.class);
        final Root<PendingRegistrationEntity> root = criteriaQuery.from(PendingRegistrationEntity.class);

        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("token"), token));

        return entityManager
                .createQuery(criteriaQuery)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public void deleteExpired(LocalDateTime cutoffTime) {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaDelete<PendingRegistrationEntity> deleteQuery = cb.createCriteriaDelete(PendingRegistrationEntity.class);
        final Root<PendingRegistrationEntity> root = deleteQuery.from(PendingRegistrationEntity.class);

        deleteQuery.where(cb.lessThan(root.get("createdAt"), cutoffTime));

        entityManager.createQuery(deleteQuery).executeUpdate();
    }

    public void deleteUsed() {
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaDelete<PendingRegistrationEntity> deleteQuery = cb.createCriteriaDelete(PendingRegistrationEntity.class);
        final Root<PendingRegistrationEntity> root = deleteQuery.from(PendingRegistrationEntity.class);

        deleteQuery.where(cb.isTrue(root.get("isUsed")));

        entityManager.createQuery(deleteQuery).executeUpdate();
    }

    private boolean isLoginExists(String login) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        final Root<UserEntity> root = criteriaQuery.from(UserEntity.class);

        criteriaQuery.select(criteriaBuilder.count(root)).where(criteriaBuilder.equal(root.get("login"), login));
        return entityManager.createQuery(criteriaQuery).getSingleResult() > 0;
    }

    private boolean isEmailExists(String email) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        final Root<UserEntity> root = criteriaQuery.from(UserEntity.class);

        criteriaQuery.select(criteriaBuilder.count(root)).where(criteriaBuilder.equal(root.get("email"), email));
        return entityManager.createQuery(criteriaQuery).getSingleResult() > 0;
    }
}
