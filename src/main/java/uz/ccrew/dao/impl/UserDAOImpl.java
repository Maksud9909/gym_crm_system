package uz.ccrew.dao.impl;

import org.hibernate.query.Query;
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
    private final UserUtils userUtils;
    private static final String ENTITY_NAME = "User";
    public static final String FIND_BY_USERNAME = "from User where username = :username";


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


    @Override
    public Optional<User> findByUsername(String username) {
        Session session = getSessionFactory().getCurrentSession();
        try {
            User user = session.createQuery(FIND_BY_USERNAME, User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            log.debug("User not found with username: {}", username);
            return Optional.empty();
        }
    }

    @Override
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
