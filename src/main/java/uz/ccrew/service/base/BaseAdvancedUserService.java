package uz.ccrew.service.base;

import uz.ccrew.dto.UserCredentials;

public interface BaseAdvancedUserService<T> extends BaseAdvancedService<T> {
    T findByUsername(String username, UserCredentials userCredentials);

    void changePassword(Long id, String newPassword, UserCredentials userCredentials);

    void activateDeactivate(Long id, Boolean isActive, UserCredentials userCredentials);
}
