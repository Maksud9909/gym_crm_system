package uz.ccrew.dao.impl;

import uz.ccrew.dao.UserDAO;
import uz.ccrew.entity.User;
import uz.ccrew.exp.exp.EntityNotFoundException;

import lombok.Getter;
import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Getter
@Repository
@RequiredArgsConstructor
public class UserDAOImpl implements UserDAO {
    private final SessionFactory sessionFactory;
    private static final String FIND_BY_USERNAME = "FROM User u WHERE u.username = :username";
    private static final String CHECK_USERNAME_QUERY = "SELECT COUNT(u) FROM User u WHERE u.username = :username";
    private static final String FIND_BY_USERNAME_AND_PASSWORD_QUERY = "FROM User u WHERE u.username = :username AND u.password = :password";

    @Override
    public boolean isUsernameExists(String username) {
        Session session = getSessionFactory().getCurrentSession();
        Long count = session.createQuery(CHECK_USERNAME_QUERY, Long.class)
                .setParameter("username", username)
                .uniqueResult();
        return count > 0;
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.createQuery(FIND_BY_USERNAME_AND_PASSWORD_QUERY, User.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
        return Optional.ofNullable(user);
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
            log.error("User with ID={} not found to change password", id);
            throw new EntityNotFoundException("User with ID=" + id + " not found to change password");
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Session session = getSessionFactory().getCurrentSession();
        User user = session.createQuery(FIND_BY_USERNAME, User.class)
                .setParameter("username", username)
                .uniqueResult();
        return Optional.ofNullable(user);
    }

    @Override
    public void activateDeactivate(String username, Boolean isActive) {
        Session session = getSessionFactory().getCurrentSession();
        User user = session.createQuery(FIND_BY_USERNAME, User.class)
                .setParameter("username", username)
                .uniqueResult();

        if (user != null) {
            user.setIsActive(isActive);
            session.merge(user);
            log.info("Updated isActive={} for User with username={}", isActive, username);
        } else {
            log.error("User with username={} not found to update isActive", username);
            throw new EntityNotFoundException("User with username=" + username + " not found to activate and deactivate profile");
        }
    }
}
