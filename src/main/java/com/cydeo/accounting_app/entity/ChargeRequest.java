package com.cydeo.accounting_app.entity;

import com.cydeo.accounting_app.enums.Currency;
import lombok.Data;

@Data
public class ChargeRequest {

    private int amount;
    private String description;
    private String stripeEmail;
    private String stripeToken;
    private Currency currency;
}