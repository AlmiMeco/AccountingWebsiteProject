package com.cydeo.accounting_app.enums;

import lombok.Getter;

@Getter
public enum CompanyStatus {

    ACTIVE("Active"), PASSIVE("Passive");

    private final String value;


    CompanyStatus(String status) {
        this.value = status;
    }

    public String getValue() {
        return value;
    }
}
