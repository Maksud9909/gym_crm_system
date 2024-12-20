package uz.ccrew.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import uz.ccrew.entity.User;

import java.security.SecureRandom;

public class UserUtils {
    private static final String DOT = ".";
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final byte PASSWORD_LENGTH = 10;
    private static final String CHECK_USERNAME_QUERY = """
                        SELECT COUNT(u) FROM Trainee u WHERE u.username = :username
            """;

    public static String generateUniqueUsername(String firstName, String lastName, SessionFactory sessionFactory) {
        String baseUsername = firstName + DOT + lastName;
        String uniqueUsername = baseUsername;
        int counter = 1;

        while (isUsernameExists(uniqueUsername, sessionFactory)) {
            uniqueUsername = baseUsername + DOT + counter++;
        }

        return uniqueUsername;
    }

    public static boolean isUsernameExists(String username, SessionFactory sessionFactory) {
        try (Session session = sessionFactory.getCurrentSession()) {
            Long count = session.createQuery(
                            CHECK_USERNAME_QUERY, Long.class)
                    .setParameter("username", username)
                    .uniqueResult();
            return count > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error checking username existence", e);
        }
    }

    public static String generateRandomPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARS.length());
            password.append(CHARS.charAt(randomIndex));
        }
        return password.toString();
    }

    public boolean authenticateUser(String username, String password) {
        try (Session session = getSessionFactory().getCurrentSession()) {
            String hql = "FROM User u WHERE u.username = :username AND u.password = :password";
            User user = session.createQuery(hql, User.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult();

            return user != null;
        } catch (Exception e) {
            log.error("Authentication failed for username: {}", username, e);
            return false;
        }
    }
}
