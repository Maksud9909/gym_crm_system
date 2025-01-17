package uz.ccrew.service.base;

import uz.ccrew.dto.user.UserCredentials;

public interface BaseAdvancedService<T, U, D> extends BaseService<T, U, D> {
    void delete(Long id, UserCredentials userCredentials);

    void update(T t, UserCredentials userCredentials);
}
