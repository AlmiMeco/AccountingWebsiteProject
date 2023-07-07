package com.cydeo.accounting_app.exception;

public class InvoiceNotFoundException extends RuntimeException{

    public InvoiceNotFoundException(String message) {
        super(message);
    }

    public InvoiceNotFoundException(String message, Throwable err) {
        super(message, err);
    }

}
