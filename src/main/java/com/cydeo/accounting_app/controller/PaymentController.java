package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.enums.Currency;
import com.cydeo.accounting_app.service.PaymentService;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final String stripePublicKey = "pk_test_51NSOnCGGEydpGaBd6DpP6VecR763l5Gdmrx3k8sonXMB2A4zf4TPjlzPDEpFfzATTEjVtzIxsUdOp2kRcki1K5zq00PEk7tSN9";

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping({"/list","/list/{year}"})
    public String paymentsList(@RequestParam(value = "year", required = false) String queryYear,  Model model){

        int yearAsInt = (queryYear == null) ? LocalDate.now().getYear() : Integer.parseInt(queryYear);

        paymentService.createPaymentsIfYearIsEmpty(yearAsInt);

        model.addAttribute("year", yearAsInt);
        model.addAttribute("payments" ,paymentService.listAllPaymentsByYear(yearAsInt));

        return "payment/payment-list";
    }

    @GetMapping("/newpayment/{id}")
    public String paymentsPayButtn(@PathVariable("id") Long id, Model model){

//        Hard Coded 'amount' for now
        model.addAttribute("amount", BigDecimal.valueOf(250*100));
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", Currency.USD);

        return "payment/payment-method";
    }

//    @GetMapping("/toInvoice/{id}")
//    public String paymentsInvoiceButtn(@PathVariable("id") Long id, Model model){
//
//        var processedPayment= paymentService.findById(id);
//
//        model.addAttribute("id", processedPayment.getId());
//        model.addAttribute("status", processedPayment.getIsPaid());
//        model.addAttribute("chargeId", processedPayment.getCompanyStripeId());
//
//        return "payment/payment-result";
//    }
}
