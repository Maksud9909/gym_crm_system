package uz.ccrew.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.ccrew.dao.UserDAO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUtilsTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserUtils userUtils;

    @BeforeEach
    void setUp() {
        userUtils = new UserUtils(userDAO);
    }

    @Test
    void generateUniqueUsername_ShouldReturnBaseUsername_WhenNoConflict() {
        String firstName = "John";
        String lastName = "Doe";
        String expectedUsername = "John.Doe";

        when(userDAO.isUsernameExists(expectedUsername)).thenReturn(false);

        String result = userUtils.generateUniqueUsername(firstName, lastName);

        assertEquals(expectedUsername, result);
    }

    @Test
    void generateUniqueUsername_ShouldReturnUniqueUsername_WhenConflictExists() {
        String firstName = "John";
        String lastName = "Doe";
        String baseUsername = "John.Doe";

        when(userDAO.isUsernameExists(baseUsername)).thenReturn(true);
        when(userDAO.isUsernameExists("John.Doe.1")).thenReturn(false);

        String result = userUtils.generateUniqueUsername(firstName, lastName);

        assertEquals("John.Doe.1", result);
    }

    @Test
    void generateRandomPassword_ShouldReturnPasswordOfCorrectLength() {
        int expectedLength = 10;

        String result = userUtils.generateRandomPassword();

        assertNotNull(result);
        assertEquals(expectedLength, result.length());
    }

    @Test
    void generateRandomPassword_ShouldReturnPasswordWithAllowedCharacters() {
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String result = userUtils.generateRandomPassword();

        assertNotNull(result);
        for (char c : result.toCharArray()) {
            assertTrue(allowedChars.contains(String.valueOf(c)));
        }
    }
}
