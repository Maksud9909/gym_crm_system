package uz.ccrew.service.base.advancedBase;

public interface BaseAdvancedUserService<T, D> extends BaseAdvancedService<T, D> {
    T findByUsername(String username);

    void changePassword(Long id, String newPassword);

    void activateDeactivate(Long id, boolean isActive);
}
