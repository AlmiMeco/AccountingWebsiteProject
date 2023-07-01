package com.cydeo.accounting_app.repository;


import com.cydeo.accounting_app.entity.Invoice;
import com.cydeo.accounting_app.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {

    @Query("SELECT i " +
            "FROM Invoice i " +
            "WHERE i.invoiceType = ?1 " +
            "AND i.company.id = ?2")
    List<Invoice> findAllByInvoiceTypeAndCompany(InvoiceType type, Long companyId);


    @Query("SELECT MAX(i.id) " +
            "FROM Invoice i " +
            "WHERE i.invoiceType = ?1 " +
            "AND i.company.id = ?2")
    long findMaxNumberInvoiceIdByCompanyIdAndType(InvoiceType type ,Long companyId);

    @Query("SELECT i.invoiceNo " +
            "FROM Invoice i " +
            "WHERE i.invoiceType = ?1 " +
            "AND i.company.id = ?2")
    List<String> findMaxInvoiceIdByType(InvoiceType type ,Long companyId);

}
