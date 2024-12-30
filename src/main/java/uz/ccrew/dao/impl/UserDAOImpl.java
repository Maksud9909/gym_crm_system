package uz.ccrew.dao.impl;

import uz.ccrew.dao.UserDAO;
import uz.ccrew.entity.User;
import uz.ccrew.utils.UserUtils;
import uz.ccrew.dao.base.base.AbstractBaseDAO;

import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class UserDAOImpl extends AbstractBaseDAO<User> implements UserDAO {
    public static final String FIND_BY_USERNAME = "SELECT u FROM User u WHERE u.username = :username";
    private final UserUtils userUtils;
    private static final String ENTITY_NAME = "User";


    public UserDAOImpl(SessionFactory sessionFactory, UserUtils userUtils) {
        super(sessionFactory, User.class);
        this.userUtils = userUtils;
    }

    @Override
    public Long create(User user) {
        Session session = getSessionFactory().getCurrentSession();

        String username = userUtils.generateUniqueUsername(
                user.getFirstName(),
                user.getLastName()
        );
        user.setUsername(username);
        user.setPassword(userUtils.generateRandomPassword());

        session.persist(user);

        Long id = user.getId();
        log.info("Created {}: ID={}, User={}", getEntityName(), id, user);

        return id;
    }

    public Optional<User> findByUsername(String username) {
        Session session = getSessionFactory().getCurrentSession();
        User user = session.createQuery(FIND_BY_USERNAME, User.class)
                .setParameter("username", username)
                .uniqueResult();
        return Optional.ofNullable(user);
    }


    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
