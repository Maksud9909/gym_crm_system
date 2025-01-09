package uz.ccrew.service.base;

import uz.ccrew.dto.UserCredentials;

import java.util.List;

public interface BaseService<T> {
    Long create(T entity);

    T findById(Long id, UserCredentials userCredentials);

    List<T> findAll(UserCredentials userCredentials);
}
