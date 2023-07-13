package com.cydeo.accounting_app.service;

import com.cydeo.accounting_app.dto.InvoiceProductDTO;
import com.cydeo.accounting_app.enums.InvoiceStatus;

import java.util.List;

public interface InvoiceProductService {

    InvoiceProductDTO findById(Long id);

    List<InvoiceProductDTO> findAllInvoiceProductsByInvoiceId(Long id);

    void saveInvoiceProduct(InvoiceProductDTO invoiceProductDTO,Long invoiceId);

    void deleteInvoiceProductById(Long id);

    void deleteInvoiceProductsByInvoiceId(Long invoiceId);

    List<InvoiceProductDTO> findAllInvoiceProductsByProductId(Long id);

    boolean isStockNotEnough(InvoiceProductDTO invoiceProductDTO, Long invoiceId);

    List<InvoiceProductDTO> findAllInvoiceProductsByInvoiceStatus(InvoiceStatus invoiceStatus);


    List<InvoiceProductDTO> findAllInvoiceProductsByStatusAndCompany();


    String productLowLimitAlert(InvoiceProductDTO invoiceProductDTO, Long invoiceId);

    void calculationProfitLossAllInvoiceProducts(Long invoiceId);
}
