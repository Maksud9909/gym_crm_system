package uz.ccrew;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableFeignClients
@SpringBootApplication
public class GymCrmSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(GymCrmSystemApplication.class, args);
    }
}
