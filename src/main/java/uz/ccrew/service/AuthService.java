package uz.ccrew.service;

import uz.ccrew.dto.user.UserCredentials;

public interface AuthService {
    void verifyUserCredentials(UserCredentials credentials);
}
