package uz.ccrew.service.impl;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginAttemptService {
    private final LoadingCache<String, Integer> attemptsCache;

    public LoginAttemptService() {
        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public void loginSucceeded(String username) {
        attemptsCache.invalidate(username);
        log.info("Login succeeded for user {}", username);
    }

    public void loginFailed(String username) {
        int attempts;
        try {
            attempts = attemptsCache.get(username) + 1;
        } catch (ExecutionException e) {
            attempts = 1;
        }
        attemptsCache.put(username, attempts);

        log.warn("Login failed for user {}", username);
    }

    public boolean isBlocked(String username) {
        try {
            return attemptsCache.get(username) >= 3;
        } catch (ExecutionException e) {
            return false;
        }
    }
}
