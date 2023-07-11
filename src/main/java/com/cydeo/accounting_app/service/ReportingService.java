package com.cydeo.accounting_app.service;

import java.math.BigDecimal;
import java.util.Map;

public interface ReportingService {

    Map<String, BigDecimal> profitLossByMonthMap();

   // Map<String, BigDecimal> findProfitLossByMonthWithCompanyId();

}
