package org.example.beans;

//import jakarta.enterprise.context.ApplicationScoped;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.example.entity.Result;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ResultService {

    @PersistenceContext(name = "default")
    private EntityManager entityManager;

    public void save(Result result) {
        entityManager.persist(result);
    }


    public void clear() {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaDelete<Result> criteriaDelete = criteriaBuilder.createCriteriaDelete(Result.class);

        criteriaDelete.from(Result.class);
        entityManager.createQuery(criteriaDelete).executeUpdate();
        entityManager.clear();
        // entityManager.createQuery("DELETE FROM Result").executeUpdate(); -- старый вариант
    }

    public List<Result> findAll() {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Result> criteriaQuery = criteriaBuilder.createQuery(Result.class);
        final Root<Result> root = criteriaQuery.from(Result.class);

        criteriaQuery
                .select(root)
                .orderBy(criteriaBuilder.desc(root.get("id")));
        return new ArrayList<>(entityManager.createQuery(criteriaQuery).getResultList());
        // List<Result> results = entityManager.createQuery("SELECT r FROM Result r ORDER BY r.id DESC", Result.class).getResultList(); -- -- старый вариант
    }
}
