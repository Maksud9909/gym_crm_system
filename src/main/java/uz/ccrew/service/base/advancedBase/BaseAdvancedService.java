package uz.ccrew.service.base.advancedBase;

import java.util.List;

public interface BaseAdvancedService<T, D, U> {
    Long create(D entity);

    T findById(Long id);

    List<T> findAll();

    void update(Long id, U entity);

    void delete(Long id);
}
