package uz.ccrew.service.base.advancedBase;

import uz.ccrew.exp.EntityNotFoundException;
import uz.ccrew.dao.base.advancedBase.BaseAdvancedCRUDDAO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractAdvancedService<T, D, U> implements BaseAdvancedService<T, D, U> {

    private BaseAdvancedCRUDDAO<T, D, U> dao;

    @Override
    @Transactional
    public Long create(D entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
        log.info("Creating {}: {}", getEntityName(), entity);
        Long id = dao.create(entity);
        log.info("{} created with ID={}", getEntityName(), id);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public T findById(Long id) {
        Objects.requireNonNull(id, "ID cannot be null");
        log.info("Finding {} by ID={}", getEntityName(), id);
        return dao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(getEntityName() + " with ID=" + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        log.info("Fetching all {}", getEntityName());
        List<T> entities = dao.findAll();
        log.info("Fetched {} {}", entities.size(), getEntityName());
        return entities;
    }

    @Override
    @Transactional
    public void update(Long id, U entity) {
        Objects.requireNonNull(id, "ID cannot be null");
        Objects.requireNonNull(entity, "Entity cannot be null");
        log.info("Updating {} with ID={}: {}", getEntityName(), id, entity);
        dao.findById(id).ifPresentOrElse(
                existing -> {
                    dao.update(id, entity);
                    log.info("{} updated with ID={}", getEntityName(), id);
                },
                () -> {
                    throw new EntityNotFoundException(getEntityName() + " with ID=" + id + " not found");
                }
        );
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Objects.requireNonNull(id, "ID cannot be null");
        log.info("Deleting {} with ID={}", getEntityName(), id);
        dao.findById(id).ifPresentOrElse(
                existing -> {
                    dao.delete(id);
                    log.info("{} deleted with ID={}", getEntityName(), id);
                },
                () -> {
                    throw new EntityNotFoundException(getEntityName() + " with ID=" + id + " not found");
                }
        );
    }

    protected abstract String getEntityName();
}
