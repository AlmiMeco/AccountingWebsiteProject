package com.cydeo.accounting_app.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message, Throwable err) {
        super(message, err);
    }
}
