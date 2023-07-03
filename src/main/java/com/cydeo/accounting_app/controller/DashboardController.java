package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final InvoiceService invoiceService;

    public DashboardController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }
    @GetMapping
    public String getDashboard(Model model) {
        model.addAttribute("invoices",invoiceService.listAllInvoices());
        return "/dashboard";
    }
}
