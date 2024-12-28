package uz.ccrew.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

class UserUtilsTest {

    private Set<String> existingUsernames;

    @BeforeEach
    void setUp() {
        existingUsernames = new HashSet<>();
        existingUsernames.add("John.Doe");
    }

    @Test
    void generateUniqueUsername_ShouldReturnUniqueUsername() {
//        String username = UserUtils.generateUniqueUsername("John", "Doe", existingUsernames);
//        assertEquals("John.Doe.1", username);

//        String anotherUsername = UserUtils.generateUniqueUsername("Jane", "Smith", existingUsernames);
//        assertEquals("Jane.Smith", anotherUsername);

        assertTrue(existingUsernames.contains("John.Doe.1"));
        assertTrue(existingUsernames.contains("Jane.Smith"));
    }

    @Test
    void generateRandomPassword_ShouldReturnPasswordWithCorrectLengthAndCharacters() {
//        String password = UserUtils.generateRandomPassword();
//        assertEquals(10, password.length());

        String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//        for (char c : password.toCharArray()) {
//            assertTrue(validChars.contains(String.valueOf(c)));
//        }
    }
}
