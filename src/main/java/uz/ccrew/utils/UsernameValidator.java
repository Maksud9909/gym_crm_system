package uz.ccrew.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class UsernameValidator {
    private static final String CHECK_USERNAME_QUERY = """
                SELECT COUNT(u) FROM Trainee u WHERE u.user.username = :username
            """;

    private final SessionFactory sessionFactory;

    @Autowired
    public UsernameValidator(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public boolean isUsernameExists(String username) {
        Session session = sessionFactory.getCurrentSession();
        Long count = session.createQuery(CHECK_USERNAME_QUERY, Long.class)
                .setParameter("username", username)
                .uniqueResult();
        return count > 0;
    }
}
