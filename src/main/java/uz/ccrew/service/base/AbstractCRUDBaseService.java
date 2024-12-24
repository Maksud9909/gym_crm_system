package uz.ccrew.service.base;

import uz.ccrew.dao.base.crudBase.BaseCRUDDAO;
import uz.ccrew.exp.EntityNotFoundException;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public abstract class AbstractCRUDBaseService<T> extends AbstractBaseService<T> implements BaseCRUDService<T> {

    public AbstractCRUDBaseService(BaseCRUDDAO<T> dao) {
        super(dao);
    }

    @Override
    public void update(Long id, T entity) {
        log.info("Updating {} with ID={}: {}", getEntityName(), id, entity);
        getDao().findById(id)
                .ifPresentOrElse(
                        existing -> {
                            ((BaseCRUDDAO<T>) getDao()).update(id, entity);
                            log.info("{} updated with ID={}", getEntityName(), id);
                        },
                        () -> {
                            throw new EntityNotFoundException(getEntityName() + " with ID=" + id + " not found");
                        }
                );
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting {} with ID={}", getEntityName(), id);
        getDao().findById(id)
                .ifPresentOrElse(
                        existing -> {
                            ((BaseCRUDDAO<T>) getDao()).delete(id);
                            log.info("{} deleted with ID={}", getEntityName(), id);
                        },
                        () -> {
                            throw new EntityNotFoundException(getEntityName() + " with ID=" + id + " not found");
                        }
                );
    }
}
