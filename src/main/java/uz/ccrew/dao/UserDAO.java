package uz.ccrew.dao;

import uz.ccrew.entity.User;

import java.util.Optional;

public interface UserDAO {
    Long create(User user);

    User findById(Long id);

    boolean isUsernameExists(String username);

    void update(User user);

    Optional<User> findByUsernameAndPassword(String username, String password);

    void changePassword(Long id, String newPassword);

    Optional<User> findByUsername(String username);

    void activateDeactivate(String username, Boolean isActive);
}
