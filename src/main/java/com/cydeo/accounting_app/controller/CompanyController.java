package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.dto.CompanyDTO;
import com.cydeo.accounting_app.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/create")
    public String companyCreate(Model model) {
        model.addAttribute("newCompany", new CompanyDTO());
        return "/company/company-create";
    }

    @GetMapping("/list")
    public String companyList(Model model) {
        model.addAttribute("companies", companyService.listAllCompanies());
        return "/company/company-list";
    }

    @GetMapping("/update/{id}")
    public String editCompany(@PathVariable("id") Long id, Model model) {
        model.addAttribute("company", companyService.findById(id));
        return "/company/company-update";
    }

    @PostMapping("/update/{id}")
    public String updateCompany(@PathVariable("id") Long id,
                                     @ModelAttribute("company") CompanyDTO companyDTO) {
        companyService.updateCompany(companyDTO);
        return "redirect:/company/list";
    }
}
