package uz.ccrew.config;

import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "GYM CRM System", version = "1.0", description = "GYM CRM System documentation"))
public class SwaggerConfig {}
