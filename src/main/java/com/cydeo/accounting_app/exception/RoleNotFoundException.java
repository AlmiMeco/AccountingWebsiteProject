package com.cydeo.accounting_app.exception;

public class RoleNotFoundException extends RuntimeException{

    public RoleNotFoundException(String message) {
        super(message);
    }

    public RoleNotFoundException(String message, Throwable err) {
        super(message, err);
    }
}
