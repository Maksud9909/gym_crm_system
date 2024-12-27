package uz.ccrew.dao.base.advancedBase;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@RequiredArgsConstructor
public abstract class AbstractAdvancedBaseCRUDDAO<T, D> implements BaseAdvancedCRUDDAO<T, D> {
    private final SessionFactory sessionFactory;
    private final Class<T> entityClass;

    @Override
    @Transactional
    public abstract Long create(D dto);

    @Override
    @Transactional
    public abstract void update(Long id, D dto);

    @Override
    @Transactional(readOnly = true)
    public Optional<T> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        T entity = session.get(entityClass, id);
        return Optional.ofNullable(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM " + entityClass.getSimpleName(), entityClass).list();
    }

    @Override
    @Transactional
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
