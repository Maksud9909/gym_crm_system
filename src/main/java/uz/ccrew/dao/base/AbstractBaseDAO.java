package uz.ccrew.dao.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractBaseDAO<T> implements BaseDAO<T, Long> {
    protected Map<Long, T> storage = new HashMap<>();
    private long idCounter = 0;

    @Override
    public abstract Long create(T entity);

    @Override
    public T findById(Long id) {
        T entity = storage.get(id);
        if (entity != null) {
            log.info("Found {} by ID={}: {}", getEntityName(), id, entity);
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
}