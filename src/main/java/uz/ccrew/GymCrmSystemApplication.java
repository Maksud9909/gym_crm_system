package uz.ccrew;

import org.springframework.boot.SpringApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableJms
@EnableFeignClients
@SpringBootApplication
public class GymCrmSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(GymCrmSystemApplication.class, args);
    }
}
