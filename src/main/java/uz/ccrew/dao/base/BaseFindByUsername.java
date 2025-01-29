package uz.ccrew.dao.base;

import java.util.Optional;

public interface BaseFindByUsername<T> {
    Optional<T> findByUsername(String username);
}
