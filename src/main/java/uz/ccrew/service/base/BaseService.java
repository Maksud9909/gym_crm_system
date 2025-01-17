package uz.ccrew.service.base;

import uz.ccrew.dto.user.UserCredentials;

import java.util.List;

public interface BaseService<T, U, D> {
    U create(D entity);

    T findById(Long id, UserCredentials userCredentials);

    List<T> findAll(UserCredentials userCredentials);
}
