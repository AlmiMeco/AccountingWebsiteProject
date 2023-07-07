package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.enums.InvoiceStatus;
import com.cydeo.accounting_app.service.InvoiceProductService;
import com.cydeo.accounting_app.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/reports")
public class StockReportController {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;

    public StockReportController(InvoiceService invoiceService, InvoiceProductService invoiceProductService) {
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
    }

    @GetMapping("/stockData")
    public String getStockReport(Model model) {
        model.addAttribute("invoiceProducts", invoiceProductService.
                findAllInvoiceProductsByInvoiceStatusAndId());
        return "/report/stock-report";
    }


}
