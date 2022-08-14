package com.ck.todoapp.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleExceptions(final Exception e) {
        HttpStatus status = HttpStatus.NOT_FOUND; // 500
        return new ResponseEntity<>(new Error(status, e.getMessage()), status);
    }
}