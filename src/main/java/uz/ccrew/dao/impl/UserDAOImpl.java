package uz.ccrew.dao.impl;

import uz.ccrew.dao.UserDAO;
import uz.ccrew.entity.User;

import lombok.Getter;
import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Getter
@Repository
public class UserDAOImpl implements UserDAO {
    private final SessionFactory sessionFactory;
    private static final String CHECK_USERNAME_QUERY = "SELECT COUNT(u) FROM User u WHERE u.username = :username";

    @Autowired
    public UserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long create(User user) {
        Session session = getSessionFactory().getCurrentSession();
        session.persist(user);
        Long id = user.getId();
        log.info("Created User:{} with ID:{}", user, id);
        return id;
    }

    @Override
    public User findById(Long id) {
        Session session = getSessionFactory().getCurrentSession();
        return session.get(User.class, id);
    }

    @Override
    public boolean isUsernameExists(String username) {
        Session session = getSessionFactory().getCurrentSession();
        Long count = session.createQuery(CHECK_USERNAME_QUERY, Long.class)
                .setParameter("username", username)
                .uniqueResult();
        return count > 0;
    }

    @Override
    public void update(User user) {
        Session session = getSessionFactory().getCurrentSession();
        session.merge(user);
        log.info("Updated User:{} ", user);
    }
}
