package uz.ccrew.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.security.SecureRandom;

public class UserUtils {
    private static final String DOT = ".";
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final byte PASSWORD_LENGTH = 10;

    public static String generateUniqueUsername(String firstName, String lastName, @NotNull Set<String> existingUsernames) {
        String baseUsername = firstName + DOT + lastName;
        String uniqueUsername = baseUsername;
        int counter = 1;

        while (existingUsernames.contains(uniqueUsername)) {
            uniqueUsername = baseUsername + DOT + counter;
            counter++;
        }

        existingUsernames.add(uniqueUsername);
        return uniqueUsername;
    }

    public static String generateRandomPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(CHARS.length());
            password.append(CHARS.charAt(randomIndex));
        }
        return password.toString();
    }
}
