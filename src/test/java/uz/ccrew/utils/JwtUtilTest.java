package uz.ccrew.utils;

import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.test.util.ReflectionTestUtils;
import uz.ccrew.security.jwt.JwtUtil;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private final String accessTokenSecretKey = "supersecretkeyforsigningaccesstokens123456789012345";
    private final String refreshTokenSecretKey = "anothersecretkeyforsigningrefreshtokens123456789012";
    private final int accessTokenTime = 3600000; // 1 час
    private final int refreshTokenTime = 604800000; // 7 дней

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        ReflectionTestUtils.setField(jwtUtil, "accessTokenSecretKey", accessTokenSecretKey);
        ReflectionTestUtils.setField(jwtUtil, "refreshTokenSecretKey", refreshTokenSecretKey);
        ReflectionTestUtils.setField(jwtUtil, "accessTokenTime", accessTokenTime);
        ReflectionTestUtils.setField(jwtUtil, "refreshTokenTime", refreshTokenTime);
    }

    @Test
    void generateAccessToken_ShouldGenerateValidToken() {
        String username = "testUser";

        String token = jwtUtil.generateAccessToken(username);

        assertNotNull(token);
        assertDoesNotThrow(() -> jwtUtil.extractUsernameFromAccessToken(token));
        assertEquals(username, jwtUtil.extractUsernameFromAccessToken(token));
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsernameFromoAccessToken() {
        String username = "testUser";
        String token = jwtUtil.generateAccessToken(username);

        String extractedUsername = jwtUtil.extractUsernameFromAccessToken(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void extractFromoAccessToken() {
        String invalidToken = "invalid.token.string";

        assertThrows(Exception.class, () -> jwtUtil.extractUsernameFromAccessToken(invalidToken));
    }

    @Test
    void getAccessTokenSignInKey_ShouldReturnValidKey() {
        Key key = jwtUtil.getAccessTokenSignInKey();

        assertNotNull(key);
        assertEquals(Keys.hmacShaKeyFor(accessTokenSecretKey.getBytes()), key);
    }

    @Test
    void getRefreshTokenSignInKey_ShouldReturnValidKey() {
        Key key = jwtUtil.getRefreshTokenSignInKey();

        assertNotNull(key);
        assertEquals(Keys.hmacShaKeyFor(refreshTokenSecretKey.getBytes()), key);
    }
}
