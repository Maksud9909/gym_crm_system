package uz.ccrew.dao.base.crudBase;

import uz.ccrew.dao.base.base.BaseDAO;

public interface BaseCRUDDAO<T> extends BaseDAO<T> {
    void update(Long id,T t);

    void delete(Long id);
}
