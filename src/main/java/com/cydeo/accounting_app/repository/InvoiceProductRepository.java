package com.cydeo.accounting_app.repository;

import com.cydeo.accounting_app.entity.Invoice;
import com.cydeo.accounting_app.entity.InvoiceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct,Long> {

    List<InvoiceProduct> findAllByInvoice(Invoice invoice);

    @Query("SELECT ip " +
            "FROM InvoiceProduct ip " +
            "WHERE ip.invoice.id = ?1")
    List<InvoiceProduct> findAllByInvoiceId(Long id);
}

