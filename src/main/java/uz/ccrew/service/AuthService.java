package uz.ccrew.service;

public interface AuthService {
    String authenticate(String username, String password);

    void logout(String token);

    boolean isAuthenticated();

    String getUsernameFromToken(String token);
}
