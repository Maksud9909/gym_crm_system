package uz.ccrew.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {
    public SimpleController() {
        System.out.println("Simple controller is created");
    }

    @GetMapping
    public String testEndpoint() {
        return "It works!";
    }
}
