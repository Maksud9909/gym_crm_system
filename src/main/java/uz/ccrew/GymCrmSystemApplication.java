package uz.ccrew;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@EnableJms
@SpringBootApplication
public class GymCrmSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(GymCrmSystemApplication.class, args);
    }
}
