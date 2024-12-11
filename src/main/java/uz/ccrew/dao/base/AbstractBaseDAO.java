package uz.ccrew.dao.base;

import java.util.*;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class AbstractBaseDAO<T> implements BaseDAO<T, Long> {
    private Map<Long, T> storage = new HashMap<>();
    private long idCounter = 0;

    @Override
    public abstract Long create(T entity);

    @Override
    public Optional<T> findById(Long id) {
        Optional<T> entity = Optional.ofNullable(storage.get(id));
        if (entity.isPresent()) {
            log.info("Found {} by ID={}: {}", getEntityName(), id, entity.get());
        } else {
            log.warn("{} not found for ID={}", getEntityName(), id);
        }
        return entity;
    }

    @Override
    public List<T> findAll() {
        log.info("Fetching all {}", getEntityName());
        return new ArrayList<>(storage.values());
    }

    protected abstract String getEntityName();

    protected Long getNextId() {
        return ++idCounter;
    }

    protected void setStorage(Map<Long, T> storage) {
        this.storage = storage;
    }
}
