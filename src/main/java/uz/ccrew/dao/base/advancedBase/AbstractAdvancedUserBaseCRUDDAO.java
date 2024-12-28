package uz.ccrew.dao.base.advancedBase;

import uz.ccrew.entity.User;
import uz.ccrew.entity.base.UserAware;
import uz.ccrew.exp.EntityNotFoundException;

import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import java.util.Optional;

@Slf4j
public abstract class AbstractAdvancedUserBaseCRUDDAO<T extends UserAware, D, U> extends AbstractAdvancedBaseCRUDDAO<T, D, U> implements BaseAdvancedUserCRUDDAO<T, D, U> {

    public static final String FIND_BY_USERNAME = "FROM %s t JOIN FETCH t.user u where u.username = :username";

    public AbstractAdvancedUserBaseCRUDDAO(SessionFactory sessionFactory, Class<T> entityClass) {
        super(sessionFactory, entityClass);
    }

    @Override
    public Optional<T> findByUsername(String username) {
        Session session = getSessionFactory().getCurrentSession();
        String hql = String.format(FIND_BY_USERNAME, getEntityName());
        return session.createQuery(hql, getEntityClass())
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        Session session = getSessionFactory().getCurrentSession();
        T entity = session.get(getEntityClass(), id);
        if (entity != null) {
            User user = entity.getUser();
            user.setPassword(newPassword);
            session.merge(user);
            log.info("Password updated for {} with ID={}", getEntityName(), id);
        } else {
            log.warn("Entity {} with ID={} not found to change password", getEntityName(), id);
            throw new EntityNotFoundException("Entity " + getEntityName() + " with ID=" + id + " not found to change password");
        }
    }

    @Override
    public void activateDeactivate(Long id, Boolean isActive) {
        Session session = getSessionFactory().getCurrentSession();
        T entity = session.get(getEntityClass(), id);
        if (entity != null) {
            User user = entity.getUser();
            user.setIsActive(isActive);
            session.merge(user);
            log.info("Updated isActive for {} ID={}", getEntityName(), id);
        } else {
            log.warn("Entity {} with ID={} not found to update isActive", getEntityName(), id);
            throw new EntityNotFoundException("Entity " + getEntityName() + " with ID=" + id + " not found to activate and Deactivate profile");
        }
    }
}
