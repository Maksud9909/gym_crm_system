package uz.ccrew.config.metrics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.sql.Connection;
import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class DBHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1000)) {
                return Health.up().withDetail("message", "Database is available").build();
            } else {
                return Health.down().withDetail("message", "Database is not available").build();
            }
        } catch (Exception e) {
            return Health.down(e).withDetail("message", "Database connection failed").build();
        }
    }
}
