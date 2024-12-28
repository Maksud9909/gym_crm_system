package uz.ccrew.dao.base.advancedBase;

import lombok.Getter;
import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@RequiredArgsConstructor
public abstract class AbstractAdvancedBaseCRUDDAO<T, D> implements BaseAdvancedCRUDDAO<T, D> {
    private final SessionFactory sessionFactory;
    private final Class<T> entityClass;
    private static final String SELECT_ALL_QUERY_TEMPLATE = "FROM %s";

    @Override
    public abstract Long create(D dto);

    @Override
    public abstract void update(Long id, D dto);

    @Override
    public Optional<T> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        T entity = session.get(entityClass, id);
        return Optional.ofNullable(entity);
    }

    @Override
    public List<T> findAll() {
        Session session = sessionFactory.getCurrentSession();
        String query = String.format(SELECT_ALL_QUERY_TEMPLATE, entityClass.getSimpleName());
        return session.createQuery(query, entityClass).list();
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();
        T entity = session.get(entityClass, id);
        if (entity != null) {
            session.remove(entity);
            log.info("Deleted {} with ID={}", getEntityName(), id);
        } else {
            log.warn("Entity {} with ID={} not found for deletion", getEntityName(), id);
        }
    }

    protected abstract String getEntityName();
}
