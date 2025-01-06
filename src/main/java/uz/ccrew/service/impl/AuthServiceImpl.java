package uz.ccrew.service.impl;

import uz.ccrew.dao.UserDAO;
import uz.ccrew.entity.User;
import uz.ccrew.service.AuthService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Getter
@Setter
@Service
public class AuthServiceImpl implements AuthService {
    private String currentUser;
    private final UserDAO userDAO;

    @Autowired
    public AuthServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
        log.debug("UserDAO injected into AuthServiceImpl");
    }

    @Transactional(readOnly = true)
    @Override
    public boolean login(String username, String password) {
        var userOpt = userDAO.findByUsername(username);
        if (userOpt.isEmpty()) return false;

        User user = userOpt.get();
        if (!user.getPassword().equals(password)) {
            return false;
        }
        currentUser = username;
        log.info("User {} is registered", username);
        return true;
    }

    @Override
    public void register(String username) {
        currentUser = username;
    }

    @Override
    public void logout() {
        currentUser = null;
        log.info("User is logged out");
    }

    @Override
    public void checkAuth() {
        if (currentUser == null) {
            throw new SecurityException("User is not authenticated!");
        }
    }
}
