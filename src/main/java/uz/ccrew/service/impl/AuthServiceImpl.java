package uz.ccrew.service.impl;

import uz.ccrew.dao.UserDAO;
import uz.ccrew.entity.User;
import uz.ccrew.service.AuthService;
import uz.ccrew.utils.SecurityContext;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserDAO userDAO;

    @Override
    public boolean login(String username, String password) {
        // Допустим, проверяем юзера в БД
        var userOpt = userDAO.findByUsername(username);
        if (userOpt.isEmpty()) return false;

        User user = userOpt.get();
        if (!user.getPassword().equals(password)) {
            return false;
        }
        // Записываем в SecurityContext
        SecurityContext.setCurrentUser(username);
        return true;
    }

    @Override
    public void logout() {
        SecurityContext.clear();
    }

    @Override
    public void checkAuth() {
        String currentUser = SecurityContext.getCurrentUser();
        if (currentUser == null) {
            throw new SecurityException("User is not authenticated!");
        }
    }
}
