package uz.ccrew.utils;

public class SessionContext {
    private static final ThreadLocal<String> currentUsername = new ThreadLocal<>();

    public static void setCurrentUser(String username) {
        currentUsername.set(username);
    }

    public static String getCurrentUser() {
        return currentUsername.get();
    }

    public static void clear() {
        currentUsername.remove();
    }
}
