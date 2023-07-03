package com.cydeo.accounting_app.service;

import com.cydeo.accounting_app.dto.CurrencyDTO;

import java.math.BigDecimal;
import java.util.Map;

public interface DashboardService{

    Map<String, BigDecimal> summaryNumbers();

    CurrencyDTO getExchangeRates();

}
