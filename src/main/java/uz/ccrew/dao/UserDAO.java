package uz.ccrew.dao;

import uz.ccrew.entity.User;

public interface UserDAO {
    Long create(User user);

    User findById(Long id);

    boolean isUsernameExists(String username);

    void update(User user);
}
