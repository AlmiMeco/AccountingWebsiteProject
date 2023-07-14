package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.service.DashboardService;
import com.cydeo.accounting_app.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final InvoiceService invoiceService;
    private final DashboardService dashboardService;

    public DashboardController(InvoiceService invoiceService, DashboardService dashboardService) {
        this.invoiceService = invoiceService;
        this.dashboardService = dashboardService;
    }
    @GetMapping
    public String getDashboard(Model model) {
        model.addAttribute("summaryNumbers",dashboardService.summaryNumbers());
        model.addAttribute("invoices",invoiceService.list3LastApprovalInvoicesForDashboard());
        model.addAttribute("exchangeRates",dashboardService.getExchangeRates());
        model.addAttribute("title", "Cydeo Accounting-Dashboard");
        return "/dashboard";
    }
}
