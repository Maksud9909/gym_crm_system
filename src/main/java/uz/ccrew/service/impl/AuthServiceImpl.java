package uz.ccrew.service.impl;

import uz.ccrew.dao.UserDAO;
import uz.ccrew.entity.User;
import uz.ccrew.dto.UserCredentials;
import uz.ccrew.exp.AuthenticationFailedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.ccrew.service.AuthService;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserDAO userDAO;

    @Autowired
    public AuthServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void verifyUserCredentials(UserCredentials credentials) {
        if (credentials == null || credentials.getUsername() == null || credentials.getPassword() == null) {
            throw new IllegalArgumentException("Username and password must not be null");
        }

        Optional<User> user = userDAO.findByUsernameAndPassword(credentials.getUsername(), credentials.getPassword());
        if (user.isEmpty()) {
            throw new AuthenticationFailedException("Invalid credentials. User not found.");
        }
    }
}
