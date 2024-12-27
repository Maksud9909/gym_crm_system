package uz.ccrew.dao;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import uz.ccrew.dao.base.base.AbstractBaseDAO;
import uz.ccrew.entity.User;

import static uz.ccrew.utils.UserUtils.*;

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
