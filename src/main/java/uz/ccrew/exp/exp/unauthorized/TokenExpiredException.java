package uz.ccrew.exp.exp.unauthorized;

public class TokenExpiredException extends Exception {
    public TokenExpiredException(String message) {
        super(message);
    }
}