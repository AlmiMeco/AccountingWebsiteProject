package com.cydeo.accounting_app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserNotFound {

    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;

    public UserNotFound(String message, Throwable throwable, HttpStatus httpStatus) {
        this.message = message;
        this.throwable = throwable;
        this.httpStatus = httpStatus;
    }
}
