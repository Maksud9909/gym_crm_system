package uz.ccrew.dao.impl;

import uz.ccrew.dao.UserDAO;
import uz.ccrew.entity.User;
import uz.ccrew.dao.base.base.AbstractBaseDAO;

import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import uz.ccrew.utils.UserUtils;

@Slf4j
@Repository
public class UserDAOImpl extends AbstractBaseDAO<User> implements UserDAO {
    private static final String ENTITY_NAME = "User";
    private final UserUtils userUtils;


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
    protected String getEntityName() {
        return ENTITY_NAME;
    }
}
