package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.annotation.ExecutionTime;
import com.cydeo.accounting_app.annotation.LoggingAnnotation;
import com.cydeo.accounting_app.client.CurrencyClient;
import com.cydeo.accounting_app.dto.CurrencyDTO;
import com.cydeo.accounting_app.dto.InvoiceDTO;
import com.cydeo.accounting_app.dto.InvoiceProductDTO;
import com.cydeo.accounting_app.dto.ProductDTO;
import com.cydeo.accounting_app.enums.InvoiceStatus;
import com.cydeo.accounting_app.enums.InvoiceType;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.service.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Service
public class DashboardServiceImpl extends LoggedInUserService implements DashboardService {

    private final InvoiceService invoiceService;
    private final CurrencyClient currencyClient;
    private final InvoiceProductService invoiceProductService;

    public DashboardServiceImpl(SecurityService securityService, MapperUtil mapperUtil, InvoiceService invoiceService, CurrencyClient currencyClient, InvoiceProductService invoiceProductService) {
        super(securityService, mapperUtil);
        this.invoiceService = invoiceService;
        this.currencyClient = currencyClient;
        this.invoiceProductService = invoiceProductService;
    }

    @Override
    public Map<String, BigDecimal> summaryNumbers() {
        /**
         * Calculating charts numbers
         */
        Map<String, BigDecimal> map = new HashMap<>();
        BigDecimal totalCost = invoiceService.listAllInvoicesForDashboardChart(InvoiceType.PURCHASE).stream()
                .map(InvoiceDTO::getPrice)
                .reduce(BigDecimal::add).orElseGet(
                        () -> new BigDecimal(0));
        BigDecimal totalSales = invoiceService.listAllInvoicesForDashboardChart(InvoiceType.SALES).stream()
                .map(InvoiceDTO::getPrice)
                .reduce(BigDecimal::add).orElseGet(
                        () -> new BigDecimal(0));
        BigDecimal profitLoss = invoiceProductService.findAllInvoiceProductsByStatusAndCompany()
                .stream()
                .filter(invoiceProductDTO -> invoiceProductDTO.getInvoice().getInvoiceType().equals(InvoiceType.SALES))
                .map(InvoiceProductDTO::getProfitLoss)
                .reduce(BigDecimal::add).orElseGet(
                        () -> new BigDecimal(0));

        map.put("totalCost", totalCost);
        map.put("totalSales", totalSales);
        map.put("profitLoss", profitLoss);
        return map;
    }

    @LoggingAnnotation
    @Override
    public CurrencyDTO getExchangeRates() {
        /**
         * Return consumed information.
         */
        return currencyClient.getCurrency();
    }
}
