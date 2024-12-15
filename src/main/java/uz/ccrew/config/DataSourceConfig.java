package uz.ccrew.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
public class DataSourceConfig {

    private final Dotenv dotenv = Dotenv.configure().load();

    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass("org.postgresql.Driver");
            dataSource.setJdbcUrl(dotenv.get("DB_URL"));
            dataSource.setUser(dotenv.get("DB_USERNAME"));
            dataSource.setPassword(dotenv.get("DB_PASSWORD"));
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
        return dataSource;
    }
}
