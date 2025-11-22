package com.gustavosdaniel.ecommerce_api.exception;

import com.gustavosdaniel.ecommerce_api.user.EmailUserDuplicationException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandle {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {

        log.warn("Validation failed {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach((fieldError) -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        ErrorResponse erros = new ErrorResponse("Validação falhou",
                "Erro de validação nos campos",
                LocalDateTime.now(),
                errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
    }

    @ExceptionHandler(EmailUserDuplicationException.class)
    public ResponseEntity<ErrorResponse> handleEmailUserDuplicationException(
            EmailUserDuplicationException ex) {

        log.warn("Email duplicado {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse("Email já em uso",
                "O email inserido já está sendo usado por outro usuário",
                LocalDateTime.now(),
                null);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
