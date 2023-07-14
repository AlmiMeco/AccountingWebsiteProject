package com.cydeo.accounting_app.repository;

import com.cydeo.accounting_app.entity.Company;
import com.cydeo.accounting_app.entity.Invoice;
import com.cydeo.accounting_app.entity.InvoiceProduct;
import com.cydeo.accounting_app.entity.Product;
import com.cydeo.accounting_app.enums.InvoiceStatus;
import com.cydeo.accounting_app.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    List<InvoiceProduct> findByInvoiceInvoiceStatusAndInvoiceCompany(InvoiceStatus invoice_invoiceStatus, Company invoice_company);

    List<InvoiceProduct> findByInvoiceInvoiceTypeAndInvoiceCompany(InvoiceType invoice_invoiceType, Company invoice_company);

    InvoiceProduct findFirstByProductAndRemainingQtyGreaterThanOrderById(Product product, int remQty);


}

