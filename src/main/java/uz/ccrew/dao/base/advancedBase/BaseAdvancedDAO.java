package uz.ccrew.dao.base.advancedBase;

import java.util.List;
import java.util.Optional;

public interface BaseAdvancedDAO<T, D> {
    Long create(D dto);

    void update(Long id, D dto);

    Optional<T> findById(Long id);

    List<T> findAll();

    void delete(Long id);
}
