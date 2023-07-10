package com.cydeo.accounting_app.exception;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message, Throwable error) {
        super(message, error);
    }
}
