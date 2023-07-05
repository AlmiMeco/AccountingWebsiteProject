package com.cydeo.accounting_app.repository;

import com.cydeo.accounting_app.entity.InvoiceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfitLossReportRepository extends JpaRepository<InvoiceProduct, Long> {


}
