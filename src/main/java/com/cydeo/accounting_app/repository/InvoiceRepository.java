package com.cydeo.accounting_app.repository;

import com.cydeo.accounting_app.entity.Invoice;
import com.cydeo.accounting_app.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
    List<Invoice> findAllByInvoiceType(InvoiceType invoiceType);

    @Query(value = "SELECT MAX(id) FROM invoices",nativeQuery = true)
    long findMaxNumberInvoiceId();

}
