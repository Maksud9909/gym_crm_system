package uz.ccrew.controller;

import uz.ccrew.dto.Response;
import uz.ccrew.exp.Unauthorized;
import uz.ccrew.dto.ResponseMaker;
import uz.ccrew.exp.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Response<?>> handleNotFound(EntityNotFoundException ex) {
        log.error("EntityNotFoundException occurred: {}", ex.getMessage());
        return ResponseMaker.error(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Response<?>> handleValidation(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException occurred: {}", ex.getMessage());
        return ResponseMaker.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<Response<?>> handleNullPointer(NullPointerException ex) {
        log.error("NullPointerException occurred: {}", ex.getMessage());
        return ResponseMaker.error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler({Unauthorized.class})
    private ResponseEntity<Response<?>> handleUnauthorized(Unauthorized ex) {
        log.error("Unauthorized occurred: {}", ex.getMessage());
        return ResponseMaker.error(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler({Exception.class})
    private ResponseEntity<Response<?>> handle(Exception e) {
        log.error("Exception occurred: {}", e.getMessage());
        return ResponseMaker.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
