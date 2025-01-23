package uz.ccrew.service.base;

import uz.ccrew.dto.user.UserCredentials;

import java.util.List;

public interface BaseService<U, C, P> {
    U create(C entity);

    P getProfile(String username);

    void activateDeactivate(String username, Boolean isActive);
}
