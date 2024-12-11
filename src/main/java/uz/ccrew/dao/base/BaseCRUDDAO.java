package uz.ccrew.dao.base;

public interface BaseCRUDDAO<T, Long> extends BaseDAO<T, Long> {
    void update(Long id,T t);

    void delete(Long id);
}
