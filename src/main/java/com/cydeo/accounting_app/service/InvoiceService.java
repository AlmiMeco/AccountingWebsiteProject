package com.cydeo.accounting_app.service;

import com.cydeo.accounting_app.dto.CompanyDTO;
import com.cydeo.accounting_app.dto.InvoiceDTO;
import com.cydeo.accounting_app.enums.InvoiceType;
import java.util.List;

public interface InvoiceService {

    InvoiceDTO findById(Long id);

    List<InvoiceDTO> listAllInvoicesByType(InvoiceType type);

    void saveInvoiceByType(InvoiceDTO invoiceDTO,InvoiceType type);

    void deleteInvoiceById(Long id);

    void approveInvoiceById(Long Id);

    InvoiceDTO createInvoice(InvoiceType type);

    String findLastInvoiceId(InvoiceType type);

    CompanyDTO getCurrentCompany();

    InvoiceDTO calculateInvoice(Long invoiceId);
    InvoiceDTO getInvoiceForPrint(Long invoiceId);
    boolean existsByClientVendorId(Long id);
    List<InvoiceDTO> listAllApprovedInvoices();

    List<InvoiceDTO> listAllInvoicesForDashboardChart(InvoiceType invoiceType);
}
