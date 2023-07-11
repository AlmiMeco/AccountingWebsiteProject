package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.entity.InvoiceProduct;
import com.cydeo.accounting_app.enums.InvoiceStatus;
import com.cydeo.accounting_app.enums.InvoiceType;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.InvoiceProductRepository;
import com.cydeo.accounting_app.service.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

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

        //BigDecimal totalSales = invoiceService.listAllInvoicesByType(InvoiceType.SALES).stream()
         //       .map(InvoiceDTO::getPrice)
          //      .reduce(BigDecimal::add).orElseGet(() -> new BigDecimal(0));


//        var profitLoss1 =  invoiceProductRepository.findInvoiceProductProfitLossByCompanyIdByInvoiceStatusByInvoiceTypeAndIsDeleted
//                (getCompany().getId(), InvoiceStatus.APPROVED, InvoiceType.SALES, false,
//                        LocalDate.of(2023,4, 1),LocalDate.of(2023, 4, 30) );
//        var profitLoss2 =  invoiceProductRepository.findInvoiceProductProfitLossByCompanyIdByInvoiceStatusByInvoiceTypeAndIsDeleted
//                (getCompany().getId(), InvoiceStatus.APPROVED, InvoiceType.SALES, false,
//                        LocalDate.of(2023,5, 1),LocalDate.of(2023, 5, 31) );

        //BigDecimal totalCost = invoiceService.listAllInvoicesByType(InvoiceType.PURCHASE).stream()
        //        .map(InvoiceDTO::getPrice)
        //        .reduce(BigDecimal::add).orElseGet(() -> new BigDecimal(0));

        //var profitLoss = totalSales.subtract(totalCost);

//        Hard-Coded for now
        //profitLostMap.put(LocalDate.now().getYear()+" "+LocalDate.now().minusMonths(3).getMonth().toString(), profitLoss1);
        //profitLostMap.put(LocalDate.now().getYear()+" "+LocalDate.now().minusMonths(2).getMonth().toString(), profitLoss2);

        return profitLostMap;
    }

    //@Override
    //public Map<String, BigDecimal> findProfitLossByMonthWithCompanyId() {
    //    Map<String, BigDecimal> map = new LinkedHashMap<>();
    //    for (int i = 0; i < invoiceProductRepository.getMonthlyDates(getCompany().getId()).size(); i++) {
     //       map.put(invoiceProductRepository.getMonthlyDates(getCompany().getId()).get(i),
      //              invoiceProductRepository.getMonthlyProfitLoss(getCompany().getId()).get(i));
        }
      //  return map;
   // }

