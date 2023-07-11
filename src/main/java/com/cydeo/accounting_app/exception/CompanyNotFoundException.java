package com.cydeo.accounting_app.exception;

public class CompanyNotFoundException extends RuntimeException {

    public CompanyNotFoundException(String message) {
        super(message);
    }
    public CompanyNotFoundException(String message, Throwable error) {
        super(message, error);
    }

}
