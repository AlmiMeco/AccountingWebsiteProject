package com.cydeo.accounting_app.repository;

import com.cydeo.accounting_app.entity.Company;
import com.cydeo.accounting_app.entity.Invoice;
import com.cydeo.accounting_app.entity.InvoiceProduct;
import com.cydeo.accounting_app.enums.InvoiceStatus;
import com.cydeo.accounting_app.enums.InvoiceType;
import com.cydeo.accounting_app.service.SecurityService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct,Long> {

    List<InvoiceProduct> findAllByInvoice(Invoice invoice);

    @Query("SELECT ip " +
            "FROM InvoiceProduct ip " +
            "WHERE ip.invoice.id = ?1")
    List<InvoiceProduct> findAllByInvoiceId(Long id);

    List<InvoiceProduct> findByProductId(Long id);
    @Query("SELECT invPr " +
            "FROM InvoiceProduct invPr JOIN Invoice i on i.id = invPr.id " +
            "WHERE i.invoiceStatus = ?1")
    List<InvoiceProduct> findAllInvoiceProductsByInvoiceStatus(InvoiceStatus invoiceStatus);

    @Query("SELECT invPr.profitLoss " +
            "FROM InvoiceProduct invPr JOIN Invoice i on i.id = invPr.id " +
            "WHERE i.company.id = ?1 AND i.invoiceStatus = ?2 AND invPr.invoice.invoiceType = ?3 AND i.isDeleted = ?4")
    BigDecimal findInvoiceProductProfitLossByCompanyIdByInvoiceStatusByInvoiceTypeAndIsDeleted(Long companyId, InvoiceStatus invoiceStatus, InvoiceType invoiceType, boolean isDeleted);


}

