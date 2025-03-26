package uz.ccrew.config.config;

import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Gym crm system API", version = "1.0", description = "Gym crm system documentation"))
@ConditionalOnProperty(value = "spring.fox.documentation.enabled", havingValue = "true", matchIfMissing = true)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer")
public class SwaggerConfig {}
