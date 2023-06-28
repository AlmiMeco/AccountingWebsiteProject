package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.dto.CompanyDTO;
import com.cydeo.accounting_app.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/create")
    public String companyCreate(Model model) {
        model.addAttribute("company", new CompanyDTO());
        return "/company/company-create";
    }

    @GetMapping("/list")
    public String companyList(Model model) {
        model.addAttribute("companyList", companyService.listAllCompanies());
        return "/company/company-list";
    }

    @GetMapping("/update/{id}")
    public String editCompany(@PathVariable("id") Long id, Model model) {
        model.addAttribute("companies", companyService.findById(id));
        return "/company/company-update";
    }
}
