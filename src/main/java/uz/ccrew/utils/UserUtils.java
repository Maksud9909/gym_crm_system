package uz.ccrew.utils;

import uz.ccrew.dao.UserDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class UserUtils {
    private final UserDAO userDAO;
    private static final String DOT = ".";
    private static final byte PASSWORD_LENGTH = 10;
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Autowired
    public UserUtils(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public String generateUniqueUsername(String firstName, String lastName) {
        String baseUsername = firstName + DOT + lastName;
        String uniqueUsername = baseUsername;
        int counter = 1;

        while (userDAO.isUsernameExists(uniqueUsername)) {
            uniqueUsername = baseUsername + DOT + counter++;
        }

        return uniqueUsername;
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
