package uz.ccrew.service.base;

public interface BaseAdvancedUserService<T> extends BaseAdvancedService<T> {
    T findByUsername(String username);

    void changePassword(Long id, String newPassword);

    void activateDeactivate(Long id, Boolean isActive);
}
