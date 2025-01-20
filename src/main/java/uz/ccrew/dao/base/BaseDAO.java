package uz.ccrew.dao.base;

public interface BaseDAO<T> {
    Long create(T entity);
}
