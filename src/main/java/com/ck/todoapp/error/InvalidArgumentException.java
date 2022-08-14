package com.ck.todoapp.error;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class InvalidArgumentException extends RuntimeException {

    private HttpStatus status = null;

    private Object data = null;

    public InvalidArgumentException() {
        super();
    }

    public InvalidArgumentException(final String message) {
        super(message);
    }

    public InvalidArgumentException(final HttpStatus status, final String message) {
        this(message);
        this.status = status;
    }

    public InvalidArgumentException(final HttpStatus status, final String message, final Object data) {
        this(status, message);
        this.data = data;
    }
}
