package uz.ccrew.security.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderConfigTest {

    @Test
    void passwordEncoder() {
        PasswordEncoderConfig config = new PasswordEncoderConfig();
        String rawPassword = "password";
        String encodedPassword = config.passwordEncoder().encode("password");
        assertTrue(config.passwordEncoder().matches(rawPassword, encodedPassword));
    }
}