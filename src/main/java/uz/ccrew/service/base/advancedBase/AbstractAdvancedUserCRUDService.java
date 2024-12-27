package uz.ccrew.service.base.advancedBase;

import lombok.extern.slf4j.Slf4j;
import uz.ccrew.dao.base.advancedBase.BaseAdvancedUserCRUDDAO;
import uz.ccrew.entity.base.UserAware;
import uz.ccrew.exp.EntityNotFoundException;

import java.util.Objects;

@Slf4j
public abstract class AbstractAdvancedUserCRUDService<T extends UserAware, D> extends AbstractAdvancedCRUDService<T, D>
        implements BaseAdvancedUserCRUDService<T, D> {

    public AbstractAdvancedUserCRUDService(BaseAdvancedUserCRUDDAO<T, D> dao) {
        super(dao);
    }

    @Override
    public T findByUsername(String username) {
        Objects.requireNonNull(username, "Username cannot be null");
        log.info("Finding {} by username={}", getEntityName(), username);
        return ((BaseAdvancedUserCRUDDAO<T, D>) getDao()).findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(getEntityName() + " with username=" + username + " not found"));
    }


    @Override
    public void changePassword(Long id, String newPassword) {
        Objects.requireNonNull(id, "ID cannot be null");
        Objects.requireNonNull(newPassword, "New password cannot be null");
        log.info("Changing password for {} with ID={}", getEntityName(), id);
        ((BaseAdvancedUserCRUDDAO<T, D>) getDao()).changePassword(id, newPassword);
    }

    @Override
    public void activateDeactivate(Long id, boolean isActive) {
        Objects.requireNonNull(id, "ID cannot be null");
        log.info("Setting isActive={} for {} with ID={}", isActive, getEntityName(), id);
        ((BaseAdvancedUserCRUDDAO<T, D>) getDao()).activateDeactivate(id, isActive);
    }
}
