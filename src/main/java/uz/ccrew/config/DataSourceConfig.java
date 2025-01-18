package uz.ccrew.config;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyVetoException;

@Configuration
public class DataSourceConfig {
    @Value("${datasource.url}")
    private String dbUrl;
    @Value("${datasource.username}")
    private String dbUsername;
    @Value("${datasource.password}")
    private String dbPassword;

    @Bean
    public DataSource dataSource() {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass("org.postgresql.Driver");
            dataSource.setJdbcUrl(dbUrl);
            dataSource.setUser(dbUsername);
            dataSource.setPassword(dbPassword);
            System.out.println("DataSource configured successfully: " + dataSource.getJdbcUrl());
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
        return dataSource;
    }
}
