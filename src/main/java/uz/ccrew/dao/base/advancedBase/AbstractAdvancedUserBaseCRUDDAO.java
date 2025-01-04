package uz.ccrew.dao.base.advancedBase;

import uz.ccrew.entity.User;
import uz.ccrew.exp.EntityNotFoundException;

import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import java.util.Optional;

@Slf4j
public abstract class AbstractAdvancedUserBaseCRUDDAO<T, D, U> extends AbstractAdvancedBaseCRUDDAO<T, D, U> implements BaseAdvancedUserCRUDDAO<T, D, U> {

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
        User user = session.get(User.class, id);
        if (user != null) {
            user.setPassword(newPassword);
            session.merge(user);
            log.info("Password updated for User with ID={}", id);
        } else {
            log.warn("User with ID={} not found to change password", id);
            throw new EntityNotFoundException("User with ID=" + id + " not found to change password");
        }
    }

    @Override
    public void activateDeactivate(Long id, Boolean isActive) {
        Session session = getSessionFactory().getCurrentSession();
        User user = session.get(User.class, id);
        if (user != null) {
            user.setIsActive(isActive);
            session.merge(user);
            log.info("Updated isActive for User with ID={}", id);
        } else {
            log.warn("User with ID={} not found to update isActive", id);
            throw new EntityNotFoundException("User with ID=" + id + " not found to activate and deactivate profile");
        }
    }


}
