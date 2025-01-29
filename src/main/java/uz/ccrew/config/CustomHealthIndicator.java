package uz.ccrew.config;

import org.springframework.stereotype.Component;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // Проверка состояния, например, внешнего API
        boolean isUp = checkExternalService();

        if (isUp) {
            return Health.up().withDetail("Service", "Available").build();
        } else {
            return Health.down().withDetail("Service", "Unavailable").build();
        }
    }

    private boolean checkExternalService() {
        // Здесь проверка, например, пинг сервиса
        return true; // Заглушка, можно заменить на реальную проверку
    }
}
