package com.cydeo.accounting_app.enums;

public enum CompanyStatus {

    ACTIVE("Active"), PASSIVE("Passive");

    private final String status;


    CompanyStatus(String status) {
        this.status = status;
    }
}
