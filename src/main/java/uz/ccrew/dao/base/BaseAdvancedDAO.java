package uz.ccrew.dao.base;

public interface BaseAdvancedDAO<T> extends BaseDAO<T>, BaseFindByUsername<T> {
    void update(T t);
}
