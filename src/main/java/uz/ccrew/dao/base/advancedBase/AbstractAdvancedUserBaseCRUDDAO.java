package uz.ccrew.dao.base.advancedBase;

import uz.ccrew.entity.User;
import uz.ccrew.entity.base.UserAware;

import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import java.util.Optional;

@Slf4j
public abstract class AbstractAdvancedUserBaseCRUDDAO<T extends UserAware, D> extends AbstractAdvancedBaseCRUDDAO<T, D> implements BaseAdvancedUserCRUDDAO<T, D> {

    public AbstractAdvancedUserBaseCRUDDAO(SessionFactory sessionFactory, Class<T> entityClass) {
        super(sessionFactory, entityClass);
    }

    @Override
    public Optional<T> findByUsername(String username) {
        Session session = getSessionFactory().getCurrentSession();
        String hql = "FROM " + getEntityName() + " t JOIN FETCH t.user u WHERE u.username = :username";
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
        }
    }

    @Override
    public void activateDeactivate(Long id, boolean isActive) {
        Session session = getSessionFactory().getCurrentSession();
        T entity = session.get(getEntityClass(), id);
        if (entity != null) {
            User user = entity.getUser();
            user.setIsActive(isActive);
            session.merge(user);
            log.info("Updated isActive for {} ID={}", getEntityName(), id);
        } else {
            log.warn("Entity {} with ID={} not found to update isActive", getEntityName(), id);
        }
    }
}
