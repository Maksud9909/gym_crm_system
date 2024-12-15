package uz.ccrew;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uz.ccrew.config.*;

public class AppStarter {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(
                DataSourceConfig.class, HibernateConfig.class
        );

        System.out.println("Hibernate tables created successfully!");
    }
}
