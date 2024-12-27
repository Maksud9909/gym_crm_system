package uz.ccrew.utils;

import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@UtilityClass
public class UsernameValidator {
    private final String CHECK_USERNAME_QUERY = """
                SELECT COUNT(u) FROM Trainee u WHERE u.user.username = :username
            """;

    private SessionFactory sessionFactory;

    public boolean isUsernameExists(String username) {
        try (Session session = sessionFactory.getCurrentSession()) {
            Long count = session.createQuery(CHECK_USERNAME_QUERY, Long.class)
                    .setParameter("username", username)
                    .uniqueResult();
            return count > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error checking username existence", e);
        }
    }
}
