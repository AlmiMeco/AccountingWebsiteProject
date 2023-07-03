package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.client.CurrencyClient;
import com.cydeo.accounting_app.dto.CurrencyDTO;
import com.cydeo.accounting_app.dto.InvoiceDTO;
import com.cydeo.accounting_app.enums.InvoiceType;
import com.cydeo.accounting_app.service.DashboardService;
import com.cydeo.accounting_app.service.InvoiceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final InvoiceService invoiceService;
    private final CurrencyClient currencyClient;

    public DashboardServiceImpl(InvoiceService invoiceService, CurrencyClient currencyClient) {
        this.invoiceService = invoiceService;
        this.currencyClient = currencyClient;
    }

    @Override
    public Map<String, BigDecimal> summaryNumbers() {
        /**
         * Calculating charts numbers
         */
        Map<String,BigDecimal> map = new HashMap<>();
        BigDecimal totalCost = invoiceService.listAllInvoicesForDashboardChart(InvoiceType.PURCHASE).stream()
                .map(InvoiceDTO::getPrice)
                .reduce(BigDecimal::add).orElseGet(
                        () ->new BigDecimal(0));
        BigDecimal totalSales = invoiceService.listAllInvoicesForDashboardChart(InvoiceType.SALES).stream()
                .map(InvoiceDTO::getPrice)
                .reduce(BigDecimal::add).orElseGet(
                        () ->new BigDecimal(0));
        BigDecimal profitLoss = totalSales.subtract(totalCost);
        map.put("totalCost",totalCost);
        map.put("totalSales",totalSales);
        map.put("profitLoss",profitLoss);
        return map;
    }

    @Override
    public CurrencyDTO getExchangeRates() {
        /**
         * Return consumed information.
         */
        return currencyClient.getCurrency();
    }
}
