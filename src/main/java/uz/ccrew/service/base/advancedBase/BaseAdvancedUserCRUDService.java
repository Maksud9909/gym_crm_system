package uz.ccrew.service.base.advancedBase;

public interface BaseAdvancedUserCRUDService<T, D> extends BaseAdvancedCRUDService<T, D> {
    T findByUsername(String username);

    void changePassword(Long id, String newPassword);

    void activateDeactivate(Long id, boolean isActive);
}
