package uz.ccrew.service.base;

import java.util.List;

public interface BaseService<T, Long> {
    Long create(T entity);

    T findById(Long id);

    List<T> findAll();
}
