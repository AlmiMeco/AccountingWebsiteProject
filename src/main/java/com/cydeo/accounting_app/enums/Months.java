package com.cydeo.accounting_app.enums;

public enum Months {

    JANUARY("January"), FEBRUARY("February"), MARCH("March"), APRIL("April"), MAY("May"),
    JUNE("June"), JULY("July"), AUGUST("August"), SEPTEMBER("September"), OCTOBER("October"),
    NOVEMBER("November"), DECEMBER("December");


    private final String value;

    Months(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
