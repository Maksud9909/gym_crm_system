package uz.ccrew.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import uz.ccrew.config.TestAppConfig;
import uz.ccrew.config.TestDataSourceConfig;
import uz.ccrew.config.TestHibernateConfig;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        TestAppConfig.class,
        TestDataSourceConfig.class,
        TestHibernateConfig.class
})
@Transactional
@Sql(scripts = "/user-util-data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserUtilsTest {
    @Autowired
    private UserUtils userUtils;

    @Test
    void generateUniqueUsername() {
        String firstName = "Maksud";
        String lastName = "Rustamov";
        String username = userUtils.generateUniqueUsername(firstName, lastName);
        assertEquals(username, firstName + "." + lastName);
    }

    @Test
    void generateRandomPassword() {
        String password = userUtils.generateRandomPassword();
        assertEquals(10, password.length());

        String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (char c : password.toCharArray()) {
            assertTrue(validChars.contains(String.valueOf(c)));
        }
    }
}
