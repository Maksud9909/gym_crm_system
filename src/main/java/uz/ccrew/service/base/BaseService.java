package uz.ccrew.service.base;

import java.util.List;

public interface BaseService<T, ID> {
    ID create(T entity);

    T findById(ID id);

    List<T> findAll();
}
