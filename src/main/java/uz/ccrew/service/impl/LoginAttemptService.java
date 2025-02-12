package uz.ccrew.service.impl;

import lombok.extern.slf4j.Slf4j;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class LoginAttemptService {
    private static final int MAX_ATTEMPT = 3;
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

    public void loginFailed(String ip) {
        int attempts = 0;
        try {
            attempts = attemptsCache.get(ip);
        } catch (ExecutionException ignored) {}

        attempts++;
        attemptsCache.put(ip, attempts);
        log.warn("Login failed for IP: {} (attempt {}/{})", ip, attempts, MAX_ATTEMPT);
    }

    public boolean isBlocked(String ip) {
        try {
            return attemptsCache.get(ip) >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
            return false;
        }
    }

    public void loginSucceeded(String ip) {
        attemptsCache.invalidate(ip);
        log.info("Login succeeded for IP: {}", ip);
    }
}
