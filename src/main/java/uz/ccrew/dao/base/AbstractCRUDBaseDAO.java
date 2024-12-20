package uz.ccrew.dao.base;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@Slf4j
@Getter
public abstract class AbstractCRUDBaseDAO<T> extends AbstractBaseDAO<T> implements BaseCRUDDAO<T> {

    public AbstractCRUDBaseDAO(SessionFactory sessionFactory, Class<T> entityClass) {
        super(sessionFactory, entityClass);
    }

    @Override
    public void update(Long id, T entity) {
        Transaction transaction = null;
        try (Session session = getSessionFactory().getCurrentSession()) {
            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
            log.info("Updated {} with ID={}: {}", getEntityName(), id, entity);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            log.error("Failed to update {}. Error: {}", getEntityName(), e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = getSessionFactory().getCurrentSession()) {
            transaction = session.beginTransaction();
            T entity = session.get(getEntityClass(), id);
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
}
