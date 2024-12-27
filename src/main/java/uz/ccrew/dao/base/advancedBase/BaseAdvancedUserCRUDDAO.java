package uz.ccrew.dao.base.advancedBase;

import java.util.Optional;

public interface BaseAdvancedUserCRUDDAO<T, D> extends BaseAdvancedCRUDDAO<T, D> {

    Optional<T> findByUsername(String username);

    void changePassword(Long id, String newPassword);

    void activateDeactivate(Long id, boolean isActive);
}
