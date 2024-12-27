package uz.ccrew.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashSet;
import java.util.Set;

@Configuration
@ComponentScan(basePackages = "uz.ccrew")
@EnableTransactionManagement
public class AppConfig {

    @Bean
    public Set<String> existingUsernames() {
        return new HashSet<>();
    }
}
