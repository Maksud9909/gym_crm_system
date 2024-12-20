package uz.ccrew;

import uz.ccrew.config.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class AppStarter {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(
                DataSourceConfig.class, HibernateConfig.class
        );
        log.info("Hibernate tables created successfully!");
    }
}
