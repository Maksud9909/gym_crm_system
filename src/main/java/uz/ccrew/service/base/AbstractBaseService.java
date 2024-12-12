package uz.ccrew.service.base;

import lombok.Getter;
import uz.ccrew.dao.base.BaseDAO;
import uz.ccrew.exp.EntityNotFoundException;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Setter
@Getter
public abstract class AbstractBaseService<T, Long> implements BaseService<T, Long> {
    private BaseDAO<T, Long> dao;

    public AbstractBaseService(BaseDAO<T, Long> dao) {
        this.dao = dao;
    }

    public AbstractBaseService() {
    }

    @Override
    public Long create(T entity) {
        log.info("Creating {}: {}", getEntityName(), entity);
        Long id = dao.create(entity);
        log.info("{} created with ID={}", getEntityName(), id);
        return id;
    }

    @Override
    public T findById(Long id) {
        log.info("Finding {} by ID={}", getEntityName(), id);
        return dao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(getEntityName() + " with ID=" + id + " not found"));
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
