package uz.ccrew.dao;

import uz.ccrew.entity.User;
import uz.ccrew.dao.base.base.AbstractBaseDAO;

import static uz.ccrew.utils.UserUtils.*;

import org.hibernate.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class UserImplDAO extends AbstractBaseDAO<User> implements UserDAO{
    private static final String ENTITY_NAME = "User";


    public UserImplDAO(SessionFactory sessionFactory) {
        super(sessionFactory, User.class);
    }

    @Override
    @Transactional
    public Long create(User user) {
        Session session = getSessionFactory().getCurrentSession();

        String username = generateUniqueUsername(
                user.getFirstName(),
                user.getLastName()
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
