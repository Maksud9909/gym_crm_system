package uz.ccrew.service.impl;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.HashSet;

@Service
public class TokenBlacklistService {
    private final Set<String> blacklistedTokens = new HashSet<>();

    public void addToBlacklist(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
