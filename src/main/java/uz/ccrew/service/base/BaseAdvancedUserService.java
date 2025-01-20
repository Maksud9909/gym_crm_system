package uz.ccrew.service.base;

import uz.ccrew.dto.user.UserCredentials;

public interface BaseAdvancedUserService<T, U, D> extends BaseAdvancedService<T, U, D> {
    T findByUsername(String username, UserCredentials userCredentials);

    void changePassword(Long id, String newPassword, UserCredentials userCredentials);

    void activateDeactivate(String username, Boolean isActive);
}
