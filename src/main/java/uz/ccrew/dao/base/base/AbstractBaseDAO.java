package uz.ccrew.dao.base.base;

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
public abstract class AbstractBaseDAO<T> implements BaseDAO<T> {
    private final SessionFactory sessionFactory;
    private final Class<T> entityClass;
    private static final String SELECT_ALL_QUERY_TEMPLATE = "FROM %s";

    @Override
    public abstract Long create(T entity);

    @Override
    public Optional<T> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        T entity = session.get(entityClass, id);
        if (entity != null) {
            log.info("Found {} by ID={}: {}", getEntityName(), id, entity);
            return Optional.of(entity);
        } else {
            log.error("{} not found for ID={}", getEntityName(), id);
            return Optional.empty();
        }
    }

    @Override
    public List<T> findAll() {
        Session session = sessionFactory.getCurrentSession();
        log.info("Fetching all {}", getEntityName());
        String query = String.format(SELECT_ALL_QUERY_TEMPLATE, entityClass.getSimpleName());
        return session.createQuery(query, entityClass).list();
    }

    protected abstract String getEntityName();
}
