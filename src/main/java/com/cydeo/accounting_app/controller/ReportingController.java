package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.service.InvoiceProductService;
import com.cydeo.accounting_app.service.ReportingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public class ReportingController {


    private final ReportingService reportingService;
    private final InvoiceProductService invoiceProductService;

    public ReportingController(ReportingService reportingService, InvoiceProductService invoiceProductService) {
        this.reportingService = reportingService;
        this.invoiceProductService = invoiceProductService;
    }

    @GetMapping("/profitLossData")
    public String getProfitLoss(Model model){
        model.addAttribute("monthlyProfitLossDataMap",reportingService.profitLossByMonthMap());
        return "report/profit-loss-report";

    }

    @GetMapping("/stockData")
    public String getStockReport(Model model) {
        model.addAttribute("invoiceProducts", invoiceProductService.findAllInvoiceProductsByStatusAndCompany());
        return "/report/stock-report";
    }

    @ModelAttribute()
    public void commonModelAttribute(Model model) {
        model.addAttribute("title", "Cydeo Accounting-Report");
    }

}
