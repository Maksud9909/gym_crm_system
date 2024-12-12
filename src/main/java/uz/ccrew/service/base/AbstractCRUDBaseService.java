package uz.ccrew.service.base;

import uz.ccrew.dao.base.BaseCRUDDAO;

import lombok.extern.slf4j.Slf4j;
import uz.ccrew.dao.base.BaseDAO;
import uz.ccrew.exp.EntityNotFoundException;

@Slf4j
public abstract class AbstractCRUDBaseService<T, Long> extends AbstractBaseService<T, Long> implements BaseCRUDService<T, Long> {

    public AbstractCRUDBaseService(BaseCRUDDAO<T, Long> dao) {
        super(dao);
    }

    public AbstractCRUDBaseService() {
    }

    @Override
    public void update(Long id, T entity) {
        log.info("Updating {} with ID={}: {}", getEntityName(), id, entity);
        getDao().findById(id)
                .ifPresentOrElse(
                        existing -> {
                            ((BaseCRUDDAO<T, Long>) getDao()).update(id, entity);
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
                            ((BaseCRUDDAO<T, Long>) getDao()).delete(id);
                            log.info("{} deleted with ID={}", getEntityName(), id);
                        },
                        () -> {
                            throw new EntityNotFoundException(getEntityName() + " with ID=" + id + " not found");
                        }
                );
    }

    public BaseDAO<T, Long> getDao() {
        return super.getDao();
    }
}
