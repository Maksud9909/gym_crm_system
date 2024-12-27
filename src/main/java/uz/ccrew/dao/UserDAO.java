package uz.ccrew.dao;

import uz.ccrew.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Long create(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
}
