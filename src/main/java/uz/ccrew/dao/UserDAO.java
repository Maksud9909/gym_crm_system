package uz.ccrew.dao;

import uz.ccrew.entity.User;
import uz.ccrew.dao.base.BaseFindByUsername;

import java.util.Optional;

public interface UserDAO extends BaseFindByUsername<User> {

    boolean isUsernameExists(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);

    void changePassword(Long id, String newPassword);

    void activateDeactivate(String username, Boolean isActive);
}
