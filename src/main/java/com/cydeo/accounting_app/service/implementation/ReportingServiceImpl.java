package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.InvoiceDTO;
import com.cydeo.accounting_app.enums.InvoiceStatus;
import com.cydeo.accounting_app.enums.InvoiceType;
import com.cydeo.accounting_app.repository.InvoiceProductRepository;
import com.cydeo.accounting_app.service.CompanyService;
import com.cydeo.accounting_app.service.InvoiceService;
import com.cydeo.accounting_app.service.ReportingService;
import com.cydeo.accounting_app.service.SecurityService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportingServiceImpl implements ReportingService {


    private final InvoiceService invoiceService;
    private final InvoiceProductRepository invoiceProductRepository;
    private final CompanyService companyService;
    private final SecurityService securityService;

    public ReportingServiceImpl(InvoiceService invoiceService, InvoiceProductRepository invoiceProductRepository, CompanyService companyService, SecurityService securityService) {
        this.invoiceService = invoiceService;
        this.invoiceProductRepository = invoiceProductRepository;
        this.companyService = companyService;
        this.securityService = securityService;
    }

    @Override
    public Map<String, BigDecimal> profitLossByMonthMap() {

        Map<String, BigDecimal> map = new HashMap<>();

        //BigDecimal totalSales = invoiceService.listAllInvoicesByType(InvoiceType.SALES).stream()
         //       .map(InvoiceDTO::getPrice)
          //      .reduce(BigDecimal::add).orElseGet(() -> new BigDecimal(0));
        var profitLoss =  invoiceProductRepository.findInvoiceProductProfitLossByCompanyIdByInvoiceStatusByInvoiceTypeAndIsDeleted
                (securityService.getLoggedInUser().getCompany().getId(), InvoiceStatus.APPROVED, InvoiceType.SALES, false);

        //BigDecimal totalCost = invoiceService.listAllInvoicesByType(InvoiceType.PURCHASE).stream()
        //        .map(InvoiceDTO::getPrice)
        //        .reduce(BigDecimal::add).orElseGet(() -> new BigDecimal(0));




        //var profitLoss = totalSales.subtract(totalCost);

//        Hard-Coded for now
        map.put(LocalDate.now().getYear()+" "+LocalDate.now().minusMonths(3).getMonth().toString(), profitLoss);

        return map;
    }
}
