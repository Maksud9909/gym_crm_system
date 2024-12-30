package uz.ccrew.utils;

public class SecurityContext {
    private static final ThreadLocal<String> currentUserHolder = new ThreadLocal<>();

    public static String getCurrentUser() {
        return currentUserHolder.get();
    }

    public static void setCurrentUser(String username) {
        currentUserHolder.set(username);
    }

    public static void clear() {
        currentUserHolder.remove();
    }

}
