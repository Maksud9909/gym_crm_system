package uz.ccrew;

import uz.ccrew.config.AppConfig;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ApplicationRunner runner = context.getBean(ApplicationRunner.class);
        runner.start();
    }
}
