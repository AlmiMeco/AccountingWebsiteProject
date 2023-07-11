package com.cydeo.accounting_app.enums;

public enum Currency {

    EUR("Euro"), USD("United States Dollar");

    private final String value;

    Currency(String value) {
        this.value = value;
    }
}
