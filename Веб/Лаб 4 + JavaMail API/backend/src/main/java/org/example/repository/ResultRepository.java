package org.example.repository;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.entity.ResultEntity;
import org.example.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class ResultRepository {

    @PersistenceContext(name = "default")
    private EntityManager entityManager;

    public void save(ResultEntity result, UserEntity user) {
        result.setUserId(user);
        entityManager.persist(result);
    }

    public void clear(Long userId) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaDelete<ResultEntity> criteriaDelete = criteriaBuilder.createCriteriaDelete(ResultEntity.class);
        final Root<ResultEntity> root = criteriaDelete.from(ResultEntity.class);

        criteriaDelete.where(criteriaBuilder.equal(root.get("user").get("id"), userId));

        entityManager.createQuery(criteriaDelete).executeUpdate();
        entityManager.clear();
    }

    public List<ResultEntity> findAll() {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<ResultEntity> criteriaQuery = criteriaBuilder.createQuery(ResultEntity.class);
        final Root<ResultEntity> root = criteriaQuery.from(ResultEntity.class);

        criteriaQuery.select(root)
                .orderBy(criteriaBuilder.desc(root.get("id")));
        return new ArrayList<>(entityManager.createQuery(criteriaQuery).getResultList());
    }
}
