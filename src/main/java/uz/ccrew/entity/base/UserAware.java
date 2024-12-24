package uz.ccrew.entity.base;

import uz.ccrew.entity.User;

public interface UserAware {
    User getUser();

    void setUser(User user);
}