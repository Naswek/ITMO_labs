package org.example.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.entity.UserEntity;

@Stateless
public class UserRepository {

    @PersistenceContext(name = "default")
    private EntityManager entityManager;
    
    public void save(UserEntity user) {
        entityManager.persist(user);
    }

    public UserEntity findById(long id) {
        return entityManager.find(UserEntity.class, id);
    }

    public UserEntity findByLogin(String login) {
        if (login == null) {
            return null;
        }

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<UserEntity> criteriaQuery = criteriaBuilder.createQuery(UserEntity.class);
        final Root<UserEntity> root = criteriaQuery.from(UserEntity.class);

        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("login"), login));

        return entityManager
                .createQuery(criteriaQuery)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public UserEntity findByEmail(String email) {
        if (email == null) {
            return null;
        }

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<UserEntity> criteriaQuery = criteriaBuilder.createQuery(UserEntity.class);
        final Root<UserEntity> root = criteriaQuery.from(UserEntity.class);

        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("email"), email));

        return entityManager
                .createQuery(criteriaQuery)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}
