package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.service.ReportingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public class ReportingController {


    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/profitLossData")
    public String getProfitLoss(Model model){

        //model.addAttribute("monthlyProfitLossDataMap", reportingService.profitLossByMonthMap());
        model.addAttribute("monthlyProfitLossDataMap",reportingService.findProfitLossByMonthWithCompanyId());
        return "report/profit-loss-report";

    }


}
