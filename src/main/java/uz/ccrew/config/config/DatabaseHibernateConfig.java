package uz.ccrew.config.config;

import lombok.extern.slf4j.Slf4j;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Objects;
import javax.sql.DataSource;
import java.util.Properties;
import java.beans.PropertyVetoException;

@Slf4j
@Configuration
@EnableTransactionManagement
public class DatabaseHibernateConfig {

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
            log.info("DataSource configured successfully: {}", dataSource.getJdbcUrl());
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("uz.ccrew.entity");

        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.show_sql", "true");
        hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
        hibernateProperties.put("hibernate.current_session_context_class", "org.springframework.orm.hibernate5.SpringSessionContext");

        sessionFactory.setHibernateProperties(hibernateProperties);
        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager(LocalSessionFactoryBean sessionFactory) {
        return new HibernateTransactionManager(Objects.requireNonNull(sessionFactory.getObject()));
    }
}