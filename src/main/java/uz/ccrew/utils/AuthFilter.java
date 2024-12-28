package uz.ccrew.utils;

import uz.ccrew.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthFilter {
    private final AuthService authService;

    @Autowired
    public AuthFilter(AuthService authService) {
        this.authService = authService;
    }

    public void checkAuthentication(String token) {
        if (token == null || !authService.isAuthenticated()) {
            throw new SecurityException("Access denied. Invalid or missing token.");
        }
    }
}
