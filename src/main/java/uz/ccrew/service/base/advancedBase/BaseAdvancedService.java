package uz.ccrew.service.base.advancedBase;

import java.util.List;

public interface BaseAdvancedService<T, D> {
    Long create(D entity);

    T findById(Long id);

    List<T> findAll();

    void update(Long id, D entity);

    void delete(Long id);
}
