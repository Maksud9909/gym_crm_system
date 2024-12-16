package uz.ccrew.dao.base;

import java.util.Optional;

public interface BaseUserDAO<T> extends BaseCRUDDAO<T> {
    Optional<T> findByUsername(String username);

    void changePassword(Long id, String newPassword);

    void activateDeactivate(Long id, boolean isActive);
}
