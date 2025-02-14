package uz.ccrew.exp;

import uz.ccrew.dto.error.ErrorResponse;
import uz.ccrew.exp.exp.EntityNotFoundException;
import uz.ccrew.exp.exp.TrainingNotAssociatedException;
import uz.ccrew.exp.exp.unauthorized.TokenExpiredException;
import uz.ccrew.exp.exp.unauthorized.BlacklistedTokenException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        log.error("EntityNotFoundException occurred: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Requested resource not found");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException occurred: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Requested parameter is invalid");
    }

    @ExceptionHandler(BlacklistedTokenException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(BlacklistedTokenException ex) {
        log.error("Token is blacklisted occurred: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Token is blacklisted. Please log in again.");
    }

    @ExceptionHandler(TrainingNotAssociatedException.class)
    public ResponseEntity<ErrorResponse> handleTrainingNotAssociatedException(TrainingNotAssociatedException ex) {
        log.error("Training association error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Training association error");
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpiredException(TokenExpiredException ex) {
        log.error("TokenExpiredException occurred: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Token expired, please renew your token");
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> handleLockedException(LockedException ex) {
        log.error("LockedException occurred: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Account is blocked for 5 minutes because of 3 login fails");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        log.error("Exception occurred: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), message, LocalDateTime.now());
        return ResponseEntity.status(status).body(errorResponse);
    }
}
