package uz.ccrew.dao;

import uz.ccrew.entity.User;
import uz.ccrew.dao.base.base.BaseDAO;

import java.util.List;
import java.util.Optional;

public interface UserDAO extends BaseDAO<User> {
    Long create(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    Optional<User> findByUsername(String username);

    boolean isUsernameExists(String username);
}
