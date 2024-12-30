package uz.ccrew.service;

public interface AuthService {

    boolean login(String username, String password);

    void logout();

    void checkAuth();
}
