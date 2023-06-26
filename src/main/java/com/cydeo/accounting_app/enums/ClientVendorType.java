package com.cydeo.accounting_app.enums;

public enum ClientVendorType {
    VENDOR_TYPE("Vendor"), CLIENT_VENDOR_TYPE("Client");
    private final String value;

    ClientVendorType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
