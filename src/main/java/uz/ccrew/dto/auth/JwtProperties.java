package uz.ccrew.dto.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;
    private long accessExpirationMs;
    private long refreshExpirationMs;
}