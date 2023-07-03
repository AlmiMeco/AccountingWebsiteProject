package com.cydeo.accounting_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/reports")
public class ReportingController {


    @GetMapping("/profitLossData")
    public String getProfitLoss(Model model){

        Map<Integer, String> map = new HashMap<>();
        map.put(1,"P/L for month 1");
        map.put(2,"P/L for month 2");
        map.put(3,"P/L for month 3");


        model.addAttribute("monthlyProfitLossDataMap", map);

        return "report/profit-loss-report";
    }


}
