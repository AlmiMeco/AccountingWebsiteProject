package com.cydeo.accounting_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @GetMapping("/list")
    public String paymentsList(){



        return "payment/payment-list";
    }

    @GetMapping("/list/{year}")
    public String paymentListWithYearQuery(){


        return "payment/payment-list";
    }
}
