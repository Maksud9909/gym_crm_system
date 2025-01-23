package uz.ccrew.service;

import uz.ccrew.dto.auth.JwtResponse;
import uz.ccrew.dto.user.UserCredentials;

public interface AuthService {
    JwtResponse login(UserCredentials userCredentials);
}
