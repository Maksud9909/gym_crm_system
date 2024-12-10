package uz.ccrew.service.base;

import uz.ccrew.dao.base.BaseDAO;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Setter
@Slf4j
public abstract class AbstractBaseService<T, ID> implements BaseService<T, ID> {
    private BaseDAO<T, ID> dao;

    public AbstractBaseService(BaseDAO<T, ID> dao) {
        this.dao = dao;
    }

    protected AbstractBaseService() {
    }

    @Override
    public ID create(T entity) {
        log.info("Creating {}: {}", getEntityName(), entity);
        ID id = dao.create(entity);
        log.info("{} created with ID={}", getEntityName(), id);
        return id;
    }

    @Override
    public T findById(ID id) {
        log.info("Finding {} by ID={}", getEntityName(), id);
        T entity = dao.findById(id);
        if (entity != null) {
            log.info("Found {}: {}", getEntityName(), entity);
        } else {
            log.warn("{} with ID={} not found", getEntityName(), id);
        }
        return entity;
    }

    @Override
    public List<T> findAll() {
        log.info("Fetching all {}", getEntityName());
        List<T> entities = dao.findAll();
        log.info("Fetched {} {}", entities.size(), getEntityName());
        return entities;
    }

    protected abstract String getEntityName();
}
