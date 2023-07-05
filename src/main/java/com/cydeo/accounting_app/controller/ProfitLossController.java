package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.service.InvoiceProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/reports")
public class ProfitLossController {

private final InvoiceProductService invoiceProductService;

    public ProfitLossController(InvoiceProductService invoiceProductService) {
        this.invoiceProductService = invoiceProductService;
    }

    @GetMapping("/profitLossData")
    public String getProfitLossReport(Model model) {
        model.addAttribute("monthlyProfitLossDataMap");
        return "/profit-loss-report.html";
    }
}