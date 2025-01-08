package uz.ccrew.dao.base;

public interface BaseCRUDDAO<T> extends BaseDAO<T> {
    void delete(Long id);

    void update(T t);
}
