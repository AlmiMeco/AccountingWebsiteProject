package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.annotation.ExecutionTime;
import com.cydeo.accounting_app.dto.ClientVendorDTO;
import com.cydeo.accounting_app.service.AddressService;
import com.cydeo.accounting_app.service.ClientVendorService;
import com.cydeo.accounting_app.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/clientVendors")
public class ClientVendorController {
    private final ClientVendorService clientVendorService;
    private final AddressService addressService;
    private final InvoiceService invoiceService;

    public ClientVendorController(ClientVendorService clientVendorService, AddressService addressService, InvoiceService invoiceService) {
        this.clientVendorService = clientVendorService;
        this.addressService = addressService;
        this.invoiceService = invoiceService;
    }
    @ExecutionTime
    @GetMapping("/list")
    public String listClientVendors(Model model) {
        model.addAttribute("clientVendors", clientVendorService.getAllClientVendors());
        return "/clientVendor/clientVendor-list";
    }

    @GetMapping("/create")
    public String showCreateClientVendorForm(Model model) {
        model.addAttribute("newClientVendor", new ClientVendorDTO());
        return "clientVendor/clientVendor-create";
    }

    @PostMapping("/create")
    public String createClientVendor(@Valid @ModelAttribute("newClientVendor") ClientVendorDTO clientVendorDTO,
                                     BindingResult bindingResult) {
        if (clientVendorService.companyNameIsExist(clientVendorDTO)) {
            bindingResult.rejectValue("clientVendorName", " ", "This Client/Vendor Name already exists.");
        }
        if (bindingResult.hasErrors()) {
            return "clientVendor/clientVendor-create";
        }

        clientVendorService.createClientVendor(clientVendorDTO);
        return "redirect:/clientVendors/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateClientVendorForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("clientVendor", clientVendorService.findById(id));
        return "/clientVendor/clientVendor-update";
    }

    @PostMapping("/update/{id}")
    public String updateClientVendor(@Valid @ModelAttribute("clientVendor") ClientVendorDTO clientVendorDTO
            , BindingResult bindingResult, @PathVariable Long id, Model model) {
        clientVendorDTO.setId(id);
        boolean companyNameIsExist = clientVendorService.companyNameIsExist(clientVendorDTO);

        if (bindingResult.hasErrors() || companyNameIsExist) {
            if (companyNameIsExist){
                bindingResult.rejectValue("clientVendorName", " ", "This Client/Vendor Name already exists.");
            }
            return "/clientVendor/clientVendor-update";
        }

        clientVendorService.updateClientVendor(id, clientVendorDTO);
        return "redirect:/clientVendors/list";
    }
    @ExecutionTime
    @GetMapping("/delete/{id}")
    public String deleteClientVendor(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        boolean hasInvoice = invoiceService.existsByClientVendorId(id);
        if (hasInvoice){
            redirectAttributes.addFlashAttribute("error","Can not be deleted you have invoices with this Client/Vendor");
            return "redirect:/clientVendors/list";
        }
        clientVendorService.deleteClientVendorById(id);
        return "redirect:/clientVendors/list";
    }

    @ModelAttribute()
    public void commonModelAttribute(Model model) {
        model.addAttribute("countries", addressService.listOfCountries());
        model.addAttribute("clientVendorTypes", clientVendorService.clientVendorType());
        model.addAttribute("title", "Accounting Cydeo-Client/Vendor");
    }
}
