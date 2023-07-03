package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.InvoiceDTO;
import com.cydeo.accounting_app.enums.InvoiceType;
import com.cydeo.accounting_app.service.InvoiceService;
import com.cydeo.accounting_app.service.ReportingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportingServiceImpl implements ReportingService {


    private final InvoiceService invoiceService;

    public ReportingServiceImpl(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Override
    public Map<String, BigDecimal> profitLossByMonthMap() {

        Map<String, BigDecimal> map = new HashMap<>();

        BigDecimal totalSales = invoiceService.listAllInvoicesForDashboardChart(InvoiceType.SALES).stream()
                .map(InvoiceDTO::getPrice)
                .reduce(BigDecimal::add).orElseGet(() -> new BigDecimal(0));

        BigDecimal totalCost = invoiceService.listAllInvoicesForDashboardChart(InvoiceType.PURCHASE).stream()
                .map(InvoiceDTO::getPrice)
                .reduce(BigDecimal::add).orElseGet(() -> new BigDecimal(0));

        var profitLoss = totalSales.subtract(totalCost);

        map.put(LocalDate.now().getMonth().toString(), profitLoss);
        return map;
    }
}
