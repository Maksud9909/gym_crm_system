package uz.ccrew.service.base;

public interface BaseCRUDService<T> extends BaseService<T> {
    void update(Long id,T entity);

    void delete(Long id);
}
