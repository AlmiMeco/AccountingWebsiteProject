package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.entity.ChargeRequest;
import com.cydeo.accounting_app.enums.Currency;
import com.cydeo.accounting_app.service.PaymentService;
import com.cydeo.accounting_app.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final StripeService stripeService;

    @Value("${stripe.public.key}")
    private String stripePublicKey;

    public PaymentController(PaymentService paymentService, StripeService stripeService) {
        this.paymentService = paymentService;
        this.stripeService = stripeService;
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

    @PostMapping("/charge")
    public String chargeResponse(ChargeRequest chargeRequest, Model model) throws StripeException {

        chargeRequest.setDescription("Example");
        chargeRequest.setCurrency(Currency.USD);

        Charge processedCharge = stripeService.charge(chargeRequest);

        model.addAttribute("id", processedCharge.getId());
        model.addAttribute("status", processedCharge.getStatus());
        model.addAttribute("chargeId", processedCharge.getId());
        model.addAttribute("balance_transaction", processedCharge.getBalanceTransaction());

        return "payment/payment-result";
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
