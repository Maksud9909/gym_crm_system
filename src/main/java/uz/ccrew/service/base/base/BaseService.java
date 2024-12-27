package uz.ccrew.service.base.base;

import java.util.List;

public interface BaseService<T> {
    Long create(T entity);

    T findById(Long id);

    List<T> findAll();
}
