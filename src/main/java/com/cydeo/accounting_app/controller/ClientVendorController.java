package com.cydeo.accounting_app.controller;
import com.cydeo.accounting_app.dto.ClientVendorDTO;
import com.cydeo.accounting_app.service.AddressService;
import com.cydeo.accounting_app.service.ClientVendorService;
import com.cydeo.accounting_app.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
                                     BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()){
            model.addAttribute("clientVendorName",bindingResult);
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
    public String updateClientVendor(@Valid @PathVariable("id") Long id,
                                     @ModelAttribute("clientVendor") ClientVendorDTO clientVendorDTO
                                     ,Model model ,BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            model.addAttribute("bindingResult", bindingResult);
            return "/clientVendor/clientVendor-update";
        }

        model.addAttribute("clientVendor",
                clientVendorService.updateClientVendor(id, clientVendorDTO));
        return "redirect:/clientVendors/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteClientVendor(@Valid @PathVariable("id") Long id,BindingResult bindingResult) {
        boolean hasInvoice = invoiceService.existsByClientVendorId(id);
        if (hasInvoice) {
            bindingResult.rejectValue("description", " ", "This clientVendor description already exists");
        }
        clientVendorService.deleteClientVendorById(id);
        return "redirect:/clientVendors/list";
    }
    @ModelAttribute()
    public void commonModelAttribute(Model model){
        model.addAttribute("countries", addressService.listOfCountries());
        model.addAttribute("clientVendorTypes", clientVendorService.clientVendorType());
        model.addAttribute("countries", clientVendorService.listOfCountry());
        model.addAttribute("title",clientVendorService.getAllClientVendors());
    }
}
