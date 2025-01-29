package uz.ccrew.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.security.Key;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${security.token.access.secret-key}")
    private String accessTokenSecretKey;
    @Value("${security.token.refresh.secret-key}")
    private String refreshTokenSecretKey;

    @Value("${security.token.access.time}")
    private int accessTokenTime;
    @Value("${security.token.refresh.time}")
    private int refreshTokenTime;

    public String generateAccessToken(String username) {
        return generateToken(username, accessTokenTime, getAccessTokenSignInKey());
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, refreshTokenTime, getRefreshTokenSignInKey());
    }

    public String extractAccessTokenUsername(String accessToken) {
        return extractClaim(accessToken, Claims::getSubject, getAccessTokenSignInKey());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver, Key key) {
        final Claims claims = extractAllClaims(token, key);
        return claimsResolver.apply(claims);
    }

    private String generateToken(String username, long expirationTime, Key key) {
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token, Key key) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    protected Key getAccessTokenSignInKey() {
        return Keys.hmacShaKeyFor(accessTokenSecretKey.getBytes());
    }

    protected Key getRefreshTokenSignInKey() {
        return Keys.hmacShaKeyFor(refreshTokenSecretKey.getBytes());
    }
}
