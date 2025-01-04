package uz.ccrew.dao.impl;

import uz.ccrew.dao.UserDAO;
import uz.ccrew.entity.User;
import uz.ccrew.dao.base.base.AbstractBaseDAO;

import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Slf4j
@Repository
public class UserDAOImpl extends AbstractBaseDAO<User> implements UserDAO {
    public static final String FIND_BY_USERNAME = "SELECT u FROM User u WHERE u.username = :username";
    private static final String CHECK_USERNAME_QUERY = "SELECT COUNT(u) FROM User u WHERE u.username = :username";
    private static final String ENTITY_NAME = "User";

    @Autowired
    public UserDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory, User.class);
    }

    @Override
    public Long create(User user) {
        Session session = getSessionFactory().getCurrentSession();
        session.persist(user);

        Long id = user.getId();
        log.info("Created {}: ID={}, User={}", getEntityName(), id, user);
        return id;
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
    public boolean isUsernameExists(String username) {
        Session session = getSessionFactory().getCurrentSession();
        Long count = session.createQuery(CHECK_USERNAME_QUERY, Long.class)
                .setParameter("username", username)
                .uniqueResult();
        return count > 0;
    }


    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
