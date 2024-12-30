package uz.ccrew.service.impl;

import uz.ccrew.dao.UserDAO;
import uz.ccrew.entity.User;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import uz.ccrew.service.AuthService;
import uz.ccrew.utils.SessionContext;

import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserDAO userDAO;
    private final Map<String, String> activeSessions = new HashMap<>(1, 0);

    @Autowired
    public AuthServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public String authenticate(String username, String password) {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = UUID.randomUUID().toString();
        activeSessions.put(token, username);
        SessionContext.setCurrentUser(username);

        return token;
    }

    public boolean isAuthenticated() {
        return SessionContext.getCurrentUser() != null;
    }

    public String getUsernameFromToken(String token) {
        return activeSessions.get(token);
    }

    public void logout(String token) {
        String username = SessionContext.getCurrentUser();
        if (username != null) {
            activeSessions.values().remove(username);
            SessionContext.clear();
        }
    }
}
