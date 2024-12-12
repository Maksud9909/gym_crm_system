package uz.ccrew.dao.base;

import java.util.List;
import java.util.Optional;

public interface BaseDAO<T> {
    Long create(T entity);

    Optional<T> findById(Long id);

    List<T> findAll();
}
