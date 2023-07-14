package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.dto.CompanyDTO;
import com.cydeo.accounting_app.dto.CountryResponseDTO;
import com.cydeo.accounting_app.service.AddressService;
import com.cydeo.accounting_app.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
        return "/company/company-create";
    }

    @PostMapping("/create")
    public String companyInsert(@Valid @ModelAttribute("newCompany") CompanyDTO companyDTO,
                                BindingResult bindingResult) {

        if (companyService.companyNameIsExist(companyDTO)) {
            bindingResult.rejectValue("title", " ", "This Company Name already exists.");
        }

        if (bindingResult.hasErrors()) {
            return "/company/company-create";
        }
        companyService.saveCompany(companyDTO);
        return "redirect:/companies/list";
    }

    @GetMapping("/list")
    public String companyList(Model model) {
        model.addAttribute("companies", companyService.listAllNonProviderCompanies());
        return "/company/company-list";
    }

    @GetMapping("/update/{id}")
    public String editCompany(@PathVariable("id") Long id, Model model) {
        model.addAttribute("company", companyService.findById(id));

        return "/company/company-update";
    }

    @PostMapping("/update/{id}")
    public String updateCompany(@Valid @ModelAttribute("company") CompanyDTO companyDTO,
                                BindingResult bindingResult, @PathVariable("id") Long id ) {
        companyDTO.setId(id);
        boolean companyNameIsExist = companyService.companyNameIsExist(companyDTO);

        if (bindingResult.hasErrors() || companyNameIsExist) {
            if (companyNameIsExist){
                bindingResult.rejectValue("title", " ", "This Title already exists.");
            }
            return "/company/company-update";
        }
        companyService.updateCompany(id,companyDTO);


        return "redirect:/companies/list";
    }

    @GetMapping("/activate/{id}")
    public String activateCompany(@PathVariable("id") Long id) {
        companyService.activateCompany(id);
        return "redirect:/companies/list";
    }

    @GetMapping ("/deactivate/{id}")
    public String deactivateCompany(@PathVariable("id") Long id) {
        companyService.deactivateCompany(id);
        return "redirect:/companies/list";
}
@ModelAttribute()
   public void commonModelAttribute(Model model) {
    model.addAttribute("countries", companyService.getListOfCountries());

   }

}
