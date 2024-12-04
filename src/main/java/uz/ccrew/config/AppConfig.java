package uz.ccrew.config;

import uz.ccrew.entity.*;

import org.springframework.context.annotation.*;

import java.util.*;

@Configuration
@ComponentScan(basePackages = "uz.ccrew")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    public Map<Long, Trainee> traineeStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, Trainer> trainerStorage() {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, Training> trainingStorage() {
        return new HashMap<>();
    }

    @Bean
    public Set<String> existingUsernames() {
        return new HashSet<>();
    }
}
