package uz.ccrew.dao.base.advancedBase;

import java.util.Optional;

public interface BaseAdvancedUserCRUDDAO<T, D, U> extends BaseAdvancedCRUDDAO<T, D, U> {

    Optional<T> findByUsername(String username);

    void changePassword(Long id, String newPassword);

    void activateDeactivate(Long id, Boolean isActive);
}
