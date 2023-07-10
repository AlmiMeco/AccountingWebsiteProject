package com.cydeo.accounting_app.exception;

public class ProductLowLimitAlertException extends RuntimeException {

    public ProductLowLimitAlertException(String message) {
        super(message);
    }

    public ProductLowLimitAlertException(String message, Throwable error) {
        super(message, error);
    }
}
