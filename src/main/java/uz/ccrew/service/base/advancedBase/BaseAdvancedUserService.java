package uz.ccrew.service.base.advancedBase;

public interface BaseAdvancedUserService<T, D, U> extends BaseAdvancedService<T, D, U> {
    T findByUsername(String username);

    void changePassword(Long id, String newPassword);

    void activateDeactivate(Long id, Boolean isActive);
}
