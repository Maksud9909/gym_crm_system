package uz.ccrew.dao.base;

import java.util.List;

public interface BaseDAO<T, ID> {
    ID create(T entity);

    T findById(ID id);

    List<T> findAll();
}
