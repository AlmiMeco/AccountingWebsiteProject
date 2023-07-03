package com.cydeo.accounting_app.service.implementation;

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

    public DashboardServiceImpl(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Override
    public Map<String, BigDecimal> summaryNumbers() {
        Map<String,BigDecimal> map = new HashMap<>();
        System.out.println(invoiceService.listAllInvoicesForDashboardChart(InvoiceType.PURCHASE));
        System.out.println(invoiceService.listAllInvoicesForDashboardChart(InvoiceType.SALES));
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
        System.out.println(map);
        return map;
    }
}
