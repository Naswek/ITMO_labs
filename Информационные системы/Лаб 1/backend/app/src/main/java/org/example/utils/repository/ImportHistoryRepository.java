package org.example.utils.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.example.entity.ImportOperation;

@ApplicationScoped
public class ImportHistoryRepository {

    @PersistenceContext(unitName = "labworks")
    private EntityManager entityManager;

    public void create(ImportOperation operation) {
        entityManager.persist(operation);
    }

    public void flush() {
        entityManager.flush();
    }

    public List<ImportOperation> list(Long userId, boolean admin) {
        if (admin) {
            return entityManager.createQuery(
                    "SELECT i FROM ImportOperation i JOIN FETCH i.user ORDER BY i.id DESC", ImportOperation.class)
                    .getResultList();
        }
        return entityManager.createQuery(
                "SELECT i FROM ImportOperation i JOIN FETCH i.user WHERE i.user.id = :userId ORDER BY i.id DESC",
                ImportOperation.class).setParameter("userId", userId).getResultList();
    }
}
