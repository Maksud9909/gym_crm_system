package uz.ccrew.service.base.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import uz.ccrew.dao.base.base.BaseDAO;
import uz.ccrew.exp.EntityNotFoundException;
import uz.ccrew.service.AuthService;

import java.util.List;
import java.util.Objects;

@Slf4j
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractBaseService<T> implements BaseService<T> {
    private BaseDAO<T> dao;
    private AuthService authService;

    @Override
    @Transactional
    public Long create(T entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
        log.info("Creating {}: {}", getEntityName(), entity);
        Long id = dao.create(entity);
        log.info("{} created with ID={}", getEntityName(), id);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public T findById(Long id) {
        authService.checkAuth();
        Objects.requireNonNull(id, "Id cannot be null");
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

    protected abstract String getEntityName();
}
