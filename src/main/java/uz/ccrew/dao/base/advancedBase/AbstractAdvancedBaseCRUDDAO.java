package uz.ccrew.dao.base.advancedBase;

import uz.ccrew.exp.EntityNotFoundException;

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
public abstract class AbstractAdvancedBaseCRUDDAO<T, D, U> implements BaseAdvancedCRUDDAO<T, D, U> {
    private final SessionFactory sessionFactory;
    private final Class<T> entityClass;
    private static final String SELECT_ALL_QUERY_TEMPLATE = "FROM %s";

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
            log.error("Entity {} with ID={} not found for deletion", getEntityName(), id);
            throw new EntityNotFoundException(getEntityName() + " with ID=" + id + " not found");
        }
    }

    protected abstract String getEntityName();
}
