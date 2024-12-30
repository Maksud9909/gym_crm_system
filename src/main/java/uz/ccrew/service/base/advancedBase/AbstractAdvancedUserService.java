package uz.ccrew.service.base.advancedBase;

import uz.ccrew.service.AuthService;
import uz.ccrew.entity.base.UserAware;
import uz.ccrew.dao.base.advancedBase.BaseAdvancedUserCRUDDAO;
import uz.ccrew.exp.EntityNotFoundException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@AllArgsConstructor
public abstract class AbstractAdvancedUserService<T extends UserAware, D, U> extends AbstractAdvancedService<T, D, U>
        implements BaseAdvancedUserService<T, D, U> {

    public AbstractAdvancedUserService(BaseAdvancedUserCRUDDAO<T, D, U> dao, AuthService authService) {
        super(authService, dao);
    }

    @Override
    @Transactional(readOnly = true)
    public T findByUsername(String username) {
        checkAuthentication();
        Objects.requireNonNull(username, "Username cannot be null");
        log.info("Finding {} by username={}", getEntityName(), username);
        return ((BaseAdvancedUserCRUDDAO<T, D, U>) getDao()).findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(getEntityName() + " with username=" + username + " not found"));
    }


    @Override
    @Transactional
    public void changePassword(Long id, String newPassword) {
        checkAuthentication();
        Objects.requireNonNull(id, "ID cannot be null");
        Objects.requireNonNull(newPassword, "New password cannot be null");
        log.info("Changing password for {} with ID={}", getEntityName(), id);
        ((BaseAdvancedUserCRUDDAO<T, D, U>) getDao()).changePassword(id, newPassword);
    }

    @Override
    @Transactional
    public void activateDeactivate(Long id, Boolean isActive) {
        checkAuthentication();
        Objects.requireNonNull(id, "ID cannot be null");
        log.info("Setting isActive={} for {} with ID={}", isActive, getEntityName(), id);
        ((BaseAdvancedUserCRUDDAO<T, D, U>) getDao()).activateDeactivate(id, isActive);
    }
}
