package uz.ccrew.service.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenBlacklistServiceTest {
    private final TokenBlacklistService tokenBlacklistService = new TokenBlacklistService();

    @BeforeEach
    void setUp() {
        tokenBlacklistService.addToBlacklist("token");
    }

    @Test
    void isBlacklisted() {
        assertTrue(tokenBlacklistService.isBlacklisted("token"));
    }

    @Test
    void addToBlacklist() {
        tokenBlacklistService.addToBlacklist("another token");
        assertTrue(tokenBlacklistService.isBlacklisted("another token"));
    }
}