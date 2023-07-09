package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping({"/list","/list/{year}"})
    public String paymentListWithYearQuery(@RequestParam(value = "year", required = false) String queryYear,  Model model){

        int yearAsInt = (queryYear == null) ? LocalDate.now().getYear() : Integer.parseInt(queryYear);

        paymentService.createPaymentsIfYearIsEmpty(yearAsInt);

        model.addAttribute("year", yearAsInt);
        model.addAttribute("payments" ,paymentService.listAllPaymentsByYear(yearAsInt));

        return "payment/payment-list";
    }
}
