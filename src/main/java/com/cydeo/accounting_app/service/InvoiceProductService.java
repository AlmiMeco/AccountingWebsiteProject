package com.cydeo.accounting_app.service;

import com.cydeo.accounting_app.dto.InvoiceProductDTO;

import java.util.List;

public interface InvoiceProductService {

    InvoiceProductDTO findById(Long id);

    List<InvoiceProductDTO> findAllInvoiceProducts();

    List<InvoiceProductDTO> findAllInvoiceProductsByInvoiceId(Long id);

    void saveInvoiceProduct(InvoiceProductDTO invoiceProductDTO, Long invoiceId);

    void deleteInvoiceProductById(Long id);

    List<InvoiceProductDTO> findAllInvoiceProductsByProductId(Long id);
}
