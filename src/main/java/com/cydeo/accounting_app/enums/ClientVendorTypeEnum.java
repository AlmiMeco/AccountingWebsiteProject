package com.cydeo.accounting_app.enums;

public enum ClientVendorTypeEnum {
    VENDOR_TYPE("Vendor"), CLIENT_VENDOR_TYPE("Client");
    private final String value;

    ClientVendorTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
