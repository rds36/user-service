package com.rds.userservice.exception;

import com.rds.securitylib.dto.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice(basePackages = "com.rds.userservice")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserExceptionHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(UserAlreadyExistException e, WebRequest request) {
        return new ResponseEntity<>(
                new ErrorResponse(
                    request.getDescription(false),
                    e.getMessage(),
                    HttpStatus.CONFLICT,
                    LocalDateTime.now()
                ),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDataNotFoundException(DataNotFoundException e, WebRequest request) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        request.getDescription(false),
                        e.getMessage(),
                        HttpStatus.NOT_FOUND,
                        LocalDateTime.now()
                ),
                HttpStatus.NOT_FOUND
        );
    }
}
