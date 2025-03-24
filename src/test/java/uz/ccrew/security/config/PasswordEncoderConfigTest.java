package uz.ccrew.security.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderConfigTest {

    @Test
    void passwordEncoder() {
        PasswordEncoderConfig config = new PasswordEncoderConfig();
        String encodedPassword = config.passwordEncoder().encode("test");
        assertTrue(config.passwordEncoder().matches("test", encodedPassword));
    }
}