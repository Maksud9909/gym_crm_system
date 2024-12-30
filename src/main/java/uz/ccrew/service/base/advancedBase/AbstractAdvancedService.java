package uz.ccrew.service.base.advancedBase;

import uz.ccrew.exp.EntityNotFoundException;
import uz.ccrew.dao.base.advancedBase.BaseAdvancedCRUDDAO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import uz.ccrew.service.AuthService;

import java.util.List;
import java.util.Objects;

@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractAdvancedService<T, D, U> implements BaseAdvancedService<T, D, U> {

    private BaseAdvancedCRUDDAO<T, D, U> dao;
    private AuthService authService;

    @Override
    @Transactional(readOnly = true)
    public T findById(Long id) {
        authService.checkAuth();
        Objects.requireNonNull(id, "ID cannot be null");
        log.info("Finding {} by ID={}", getEntityName(), id);
        return dao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(getEntityName() + " with ID=" + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        authService.checkAuth();
        log.info("Fetching all {}", getEntityName());
        List<T> entities = dao.findAll();
        log.info("Fetched {} {}", entities.size(), getEntityName());
        return entities;
    }

    @Override
    @Transactional
    public void update(Long id, U entity) {
        authService.checkAuth();
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
        authService.checkAuth();
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
