package uz.ccrew.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.security.SecureRandom;

public class UserUtils {

    public static String generateUniqueUsername(String firstName, String lastName, @NotNull Set<String> existingUsernames) {
        String baseUsername = firstName + "." + lastName;
        String uniqueUsername = baseUsername;
        int counter = 1;

        while (existingUsernames.contains(uniqueUsername)) {
            uniqueUsername = baseUsername + "." + counter;
            counter++;
        }

        existingUsernames.add(uniqueUsername);
        return uniqueUsername;
    }

    public static String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder(10);
        SecureRandom random = new SecureRandom(); // генерирует криптографически стойкие случайные числа
        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(chars.length());
            password.append(chars.charAt(randomIndex));
        }
        return password.toString();
    }
}
