package uz.ccrew.dao.base.advancedBase;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@RequiredArgsConstructor
public abstract class AbstractAdvancedBaseDAO<T, D> implements BaseAdvancedDAO<T, D> {
    private final SessionFactory sessionFactory;
    private final Class<T> entityClass;

    @Override
    public abstract Long create(D dto);

    @Override
    public abstract void update(Long id, D dto);

    @Override
    public Optional<T> findById(Long id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            T entity = session.get(entityClass, id);
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public List<T> findAll() {
        try (Session session = sessionFactory.getCurrentSession()) {
            return session.createQuery("FROM " + entityClass.getSimpleName(), entityClass).list();
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.getCurrentSession()) {
            transaction = session.beginTransaction();
            T entity = session.get(entityClass, id);
            if (entity != null) {
                session.delete(entity);
                transaction.commit();
                log.info("Deleted {} with ID={}", getEntityName(), id);
            } else {
                log.warn("{} with ID={} not found for deletion", getEntityName(), id);
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            log.error("Failed to delete {}. Error: {}", getEntityName(), e.getMessage());
        }
    }

    protected abstract String getEntityName();
}
