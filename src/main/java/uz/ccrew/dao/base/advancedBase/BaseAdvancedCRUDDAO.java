package uz.ccrew.dao.base.advancedBase;

import java.util.List;
import java.util.Optional;

public interface BaseAdvancedCRUDDAO<T, D, U> {
    Long create(D dto);

    void update(Long id, U dto);

    Optional<T> findById(Long id);

    List<T> findAll();

    void delete(Long id);
}
