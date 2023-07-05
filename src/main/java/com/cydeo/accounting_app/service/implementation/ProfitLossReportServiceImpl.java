package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.InvoiceProductDTO;
import com.cydeo.accounting_app.entity.InvoiceProduct;
import com.cydeo.accounting_app.enums.InvoiceStatus;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.ProfitLossReportRepository;
import com.cydeo.accounting_app.service.InvoiceProductService;
import com.cydeo.accounting_app.service.ProfitLossReportService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfitLossReportServiceImpl implements ProfitLossReportService {

    private final InvoiceProductService invoiceProductService;
    private final ProfitLossReportRepository profitLossReportRepository;
    private final InvoiceStatus invoiceStatus;
    private final MapperUtil mapperUtil;

    public ProfitLossReportServiceImpl(InvoiceProductService invoiceProductService, ProfitLossReportRepository profitLossReportRepository, InvoiceStatus invoiceStatus, MapperUtil mapperUtil) {
        this.invoiceProductService = invoiceProductService;
        this.profitLossReportRepository = profitLossReportRepository;
        this.invoiceStatus = invoiceStatus;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<InvoiceProductDTO> listOfInvoiceProductsById(Long id) {
        return invoiceProductService.findAllInvoiceProductsByInvoiceId(id);
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
