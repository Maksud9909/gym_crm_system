package uz.ccrew.dao.base;

public interface BaseCRUDDAO<T> extends BaseDAO<T> {
    void update(Long id,T t);

    void delete(Long id);
}
