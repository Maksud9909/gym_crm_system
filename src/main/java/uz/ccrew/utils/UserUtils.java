package uz.ccrew.utils;

import static uz.ccrew.utils.UsernameValidator.*;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class UserUtils {
    private final String DOT = ".";
    private final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final byte PASSWORD_LENGTH = 10;

    private UsernameValidator usernameValidator;

    public String generateUniqueUsername(String firstName, String lastName) {
        String baseUsername = firstName + DOT + lastName;
        String uniqueUsername = baseUsername;
        int counter = 1;

        while (isUsernameExists(uniqueUsername)) {
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
