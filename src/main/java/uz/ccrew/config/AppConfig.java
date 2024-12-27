package uz.ccrew.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
@ComponentScan(basePackages = "uz.ccrew")
public class AppConfig {

    @Bean
    public Set<String> existingUsernames() {
        return new HashSet<>();
    }
}
