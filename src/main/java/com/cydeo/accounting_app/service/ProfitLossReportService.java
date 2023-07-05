package com.cydeo.accounting_app.service;

import com.cydeo.accounting_app.entity.InvoiceProduct;

import java.util.List;


public interface ProfitLossReportService {

    List<InvoiceProduct> listOfInvoiceProducts();

    Integer getTotalProfitLoss();

}
