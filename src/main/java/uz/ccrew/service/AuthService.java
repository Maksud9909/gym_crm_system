package uz.ccrew.service;

import uz.ccrew.dto.auth.JwtResponse;
import uz.ccrew.dto.user.UserCredentials;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    JwtResponse login(UserCredentials userCredentials, HttpServletRequest request);
}
