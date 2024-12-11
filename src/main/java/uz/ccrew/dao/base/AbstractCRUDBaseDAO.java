package uz.ccrew.dao.base;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class AbstractCRUDBaseDAO<T> extends AbstractBaseDAO<T> implements BaseCRUDDAO<T,Long>{

    @Override
    public void update(Long id, T entity) {
        if (getStorage().containsKey(id)) {
            getStorage().put(id, entity);
            log.info("Updated {} with ID={}: {}", getEntityName(), id, entity);
        } else {
            log.warn("Failed to update {}. ID={} not found", getEntityName(), id);
        }
    }

    @Override
    public void delete(Long id) {
        if (getStorage().remove(id) != null) {
            log.info("Deleted {} with ID={}", getEntityName(), id);
        } else {
            log.warn("Failed to delete {}. ID={} not found", getEntityName(), id);
        }
    }
}
