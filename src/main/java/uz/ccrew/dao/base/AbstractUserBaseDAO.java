package uz.ccrew.dao.base;

import uz.ccrew.entity.User;

import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import java.util.Optional;

@Slf4j
public abstract class AbstractUserBaseDAO<T> extends AbstractCRUDBaseDAO<T> implements BaseUserDAO<T> {

    public AbstractUserBaseDAO(SessionFactory sessionFactory, Class<T> entityClass) {
        super(sessionFactory, entityClass);
    }

    @Override
    public Optional<T> findByUsername(String username) {
        try (Session session = getSessionFactory().getCurrentSession()) {
            String hql = "FROM " + getEntityName() + " u WHERE u.username = :username";
            return session.createQuery(hql, getEntityClass())
                    .setParameter("username", username)
                    .uniqueResultOptional();
        }
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        try (Session session = getSessionFactory().getCurrentSession()) {
            T entity = session.get(getEntityClass(), id);
            if (entity instanceof User) {
                ((User) entity).setPassword(newPassword);
                session.merge(entity);
                log.info("Password updated for {} with ID={}", getEntityName(), id);
            }
        } catch (Exception e) {
            log.error("Failed to update password for {} ID={}", getEntityName(), id, e);
        }
    }

    @Override
    public void activateDeactivate(Long id, boolean isActive) {
        try (Session session = getSessionFactory().getCurrentSession()) {
            T entity = session.get(getEntityClass(), id);
            if (entity instanceof User) {
                ((User) entity).setIsActive(isActive);
                session.merge(entity);
                log.info("Updated isActive for {} ID={}", getEntityName(), id);
            }
        } catch (Exception e) {
            log.error("Failed to update isActive for {} ID={}", getEntityName(), id, e);
        }
    }
}
