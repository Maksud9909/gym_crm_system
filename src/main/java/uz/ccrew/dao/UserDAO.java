package uz.ccrew.dao;

import jakarta.transaction.Transactional;
import uz.ccrew.entity.User;
import uz.ccrew.dao.base.AbstractBaseDAO;

import static uz.ccrew.utils.UserUtils.generateRandomPassword;
import static uz.ccrew.utils.UserUtils.generateUniqueUsername;

import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class UserDAO extends AbstractBaseDAO<User> {
    private static final String ENTITY_NAME = "User";

    public UserDAO(SessionFactory sessionFactory, Class<User> entityClass) {
        super(sessionFactory, entityClass);
    }

    @Override
    @Transactional
    public Long create(User user) {
        Session session = getSessionFactory().getCurrentSession();

        String username = generateUniqueUsername(
                user.getFirstName(),
                user.getLastName(),
                session.getSessionFactory()
        );
        user.setUsername(username);
        user.setPassword(generateRandomPassword());

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
