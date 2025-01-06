package uz.ccrew.service;

public interface AuthService {

    void register(String username);

    boolean login(String username, String password);

    void logout();

    void checkAuth();
}
