package uz.ccrew.service.base;

public interface BaseAdvancedService<T> extends BaseService<T> {
    void delete(Long id);

    void update(T t);
}
