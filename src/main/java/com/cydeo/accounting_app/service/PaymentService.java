package com.cydeo.accounting_app.service;

import com.cydeo.accounting_app.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {

    List<PaymentDTO> listAllPaymentsByYear(int year);

    void createPaymentsIfYearIsEmpty(int year);

    PaymentDTO findById(Long id);

    PaymentDTO update(Long id);

}
