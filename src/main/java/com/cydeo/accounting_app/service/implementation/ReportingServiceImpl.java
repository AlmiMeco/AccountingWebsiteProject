package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.entity.InvoiceProduct;
import com.cydeo.accounting_app.enums.InvoiceType;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.InvoiceProductRepository;
import com.cydeo.accounting_app.service.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

import java.util.*;

@Service
public class ReportingServiceImpl extends LoggedInUserService implements ReportingService {

    private final InvoiceProductRepository invoiceProductRepository;


    public ReportingServiceImpl(SecurityService securityService, MapperUtil mapperUtil, InvoiceProductRepository invoiceProductRepository) {
        super(securityService, mapperUtil);
        this.invoiceProductRepository = invoiceProductRepository;

    }


    @Override
    public Map<String, BigDecimal> profitLossByMonthMap() {
        Map<String, BigDecimal> profitLostMap = new LinkedHashMap<>();

        List<InvoiceProduct> salesInvoiceProducts = invoiceProductRepository.findByInvoiceInvoiceTypeAndInvoiceCompany(InvoiceType.SALES, getCompany());

        for (InvoiceProduct salesInvoiceProduct : salesInvoiceProducts) {
            int year = salesInvoiceProduct.getInvoice().getDate().getYear();
            String month = salesInvoiceProduct.getInvoice().getDate().getMonth().toString();
            BigDecimal profitLoss = salesInvoiceProduct.getProfitLoss();
            String yearMonth = year + " " + month;
            profitLostMap.put(yearMonth,profitLostMap.getOrDefault(yearMonth, BigDecimal.ZERO).add(profitLoss));
        }

        return profitLostMap;
    }
        }


