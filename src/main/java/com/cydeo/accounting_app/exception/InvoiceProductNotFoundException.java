package com.cydeo.accounting_app.exception;

public class InvoiceProductNotFoundException extends RuntimeException{

    public InvoiceProductNotFoundException(String message) {
        super(message);
    }

    public InvoiceProductNotFoundException(String message, Throwable err) {
        super(message, err);
    }
}
