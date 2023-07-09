package com.cydeo.accounting_app.repository;

import com.cydeo.accounting_app.entity.Company;
import com.cydeo.accounting_app.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findAllByCompanyAndYear(Company company, int year);

}
