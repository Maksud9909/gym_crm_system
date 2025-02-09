package uz.ccrew.exp;

import uz.ccrew.exp.exp.EntityNotFoundException;
import uz.ccrew.exp.exp.TrainingNotAssociatedException;
import uz.ccrew.exp.exp.unauthorized.UnauthorizedException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<?> handleNotFound(EntityNotFoundException ex) {
        log.error("EntityNotFoundException occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Requested resource not found");
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> handleValidation(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Requested parameter is invalid");
    }

    @ExceptionHandler({UnauthorizedException.class})
    private ResponseEntity<String> handleUnauthorized(UnauthorizedException ex) {
        log.error("Unauthorized occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Unauthorized");
    }

    @ExceptionHandler({TrainingNotAssociatedException.class})
    public ResponseEntity<String> handleTrainingNotAssociatedException(TrainingNotAssociatedException ex) {
        log.error("Training association error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Training association error");
    }

    @ExceptionHandler({LockedException.class})
    public ResponseEntity<String> handleLockedException(LockedException ex) {
        log.error("LockedException occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Account is blocked for 5 minutes because of 3 login fails");
    }

    @ExceptionHandler({Exception.class})
    private ResponseEntity<String> handle(Exception e) {
        log.error("Exception occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal Server Error");
    }
}
