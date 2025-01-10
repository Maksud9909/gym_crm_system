package uz.ccrew.service.base;

import uz.ccrew.dto.UserCredentials;

public interface BaseAdvancedService<T> extends BaseService<T> {
    void delete(Long id, UserCredentials userCredentials);

    void update(T t, UserCredentials userCredentials);
}
