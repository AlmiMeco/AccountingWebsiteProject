package com.cydeo.accounting_app.service;

import java.math.BigDecimal;
import java.util.Map;

public interface DashboardService{

    Map<String, BigDecimal> summaryNumbers();
}
