package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.entity.InvoiceProduct;
import com.cydeo.accounting_app.service.InvoiceProductService;
import com.cydeo.accounting_app.service.ProfitLossReportService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfitLossReportServiceImpl implements ProfitLossReportService {

    private final InvoiceProductService invoiceProductService;

    public ProfitLossReportServiceImpl(InvoiceProductService invoiceProductService) {
        this.invoiceProductService = invoiceProductService;
    }

    @Override
    public List<InvoiceProduct> listOfInvoiceProducts() {
        return null;
    }

    @Override
    public Integer getTotalProfitLoss() {
        return null;
    }
}
