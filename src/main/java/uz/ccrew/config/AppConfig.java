package uz.ccrew.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "uz.ccrew")
@EnableTransactionManagement
public class AppConfig {}
