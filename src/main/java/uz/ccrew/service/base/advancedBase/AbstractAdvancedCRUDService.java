package uz.ccrew.service.base.advancedBase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uz.ccrew.dao.base.advancedBase.BaseAdvancedCRUDDAO;
import uz.ccrew.exp.EntityNotFoundException;

import java.util.List;
import java.util.Objects;

@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractAdvancedCRUDService<T, D> implements BaseAdvancedCRUDService<T, D> {

    private BaseAdvancedCRUDDAO<T, D> dao;

    @Override
    public Long create(D entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
        log.info("Creating {}: {}", getEntityName(), entity);
        Long id = dao.create(entity);
        log.info("{} created with ID={}", getEntityName(), id);
        return id;
    }

    @Override
    public T findById(Long id) {
        Objects.requireNonNull(id, "ID cannot be null");
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

    @Override
    public void update(Long id, D entity) {
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
