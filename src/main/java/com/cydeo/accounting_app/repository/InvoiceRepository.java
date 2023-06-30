package com.cydeo.accounting_app.repository;

import com.cydeo.accounting_app.entity.Company;
import com.cydeo.accounting_app.entity.Invoice;
import com.cydeo.accounting_app.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {

    @Query("SELECT i " +
            "FROM Invoice i " +
            "WHERE i.invoiceType = ?1 " +
            "AND i.company.id = ?2")
    List<Invoice> findAllByInvoiceTypeAndCompany(InvoiceType type, Long companyId);

    @Query(value = "SELECT MAX(id) FROM invoices",nativeQuery = true)
    long findMaxNumberInvoiceId();

    @Query(value = "SELECT invoice_no FROM invoices i " +
            "WHERE i.invoice_type = ?1",nativeQuery = true)
    List<String> findMaxInvoiceIdByType(@Param("invoice_type")String type);

}
