package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.dto.CompanyDTO;
import com.cydeo.accounting_app.service.AddressService;
import com.cydeo.accounting_app.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final AddressService addressService;


    public CompanyController(CompanyService companyService, AddressService addressService) {
        this.companyService = companyService;
        this.addressService = addressService;
    }

    @GetMapping("/create")
    public String companyCreate(Model model) {
        model.addAttribute("newCompany", new CompanyDTO());
        model.addAttribute("countries", List.of(addressService.listOfCountries()));
        return "/company/company-create";
    }

    @PostMapping("/create")
    public String companyInsert(@ModelAttribute("newCompany") CompanyDTO companyDTO) {
        companyService.saveCompany(companyDTO);
        return "redirect:/company/company-create";
    }

    @GetMapping("/list")
    public String companyList(Model model) {
        model.addAttribute("companies", companyService.listAllCompanies());
        return "/company/company-list";
    }

    @GetMapping("/update/{id}")
    public String editCompany(@PathVariable("id") Long id, Model model) {
        model.addAttribute("company", companyService.findById(id));
        model.addAttribute("countries", List.of(addressService.listOfCountries()).get(0));

        return "/company/company-update";
    }

    @PostMapping("/update/{id}")
    public String updateCompany(@PathVariable("id") Long id, @ModelAttribute("company")
    CompanyDTO companyDTO) {
       companyService.updateCompany(id,companyDTO);

        return "redirect:/companies-update";
    }
}
