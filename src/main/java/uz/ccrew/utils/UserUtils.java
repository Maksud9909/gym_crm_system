package uz.ccrew.utils;

import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.security.SecureRandom;

@UtilityClass
public class UserUtils {
    private final String DOT = ".";
    private final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final byte PASSWORD_LENGTH = 10;
    private final String CHECK_USERNAME_QUERY = """
                        SELECT COUNT(u) FROM Trainee u WHERE u.username = :username
            """;

    public String generateUniqueUsername(String firstName, String lastName, SessionFactory sessionFactory) {
        String baseUsername = firstName + DOT + lastName;
        String uniqueUsername = baseUsername;
        int counter = 1;

        while (isUsernameExists(uniqueUsername, sessionFactory)) {
            uniqueUsername = baseUsername + DOT + counter++;
        }

        return uniqueUsername;
    }

    public boolean isUsernameExists(String username, SessionFactory sessionFactory) {
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

    public String generateRandomPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARS.length());
            password.append(CHARS.charAt(randomIndex));
        }
        return password.toString();
    }
}