package uz.ccrew.service;

import uz.ccrew.dto.UserCredentials;

public interface AuthService {
    void verifyUserCredentials(UserCredentials credentials);
}
