package uz.ccrew.service.base;

public interface BaseCRUDService<T, Long> extends BaseService<T, Long> {
    void update(Long id,T entity);

    void delete(Long id);
}
