package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.dto.ClientVendorDTO;
import com.cydeo.accounting_app.service.ClientVendorService;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientVendors")
public class ClientVendorController {
    private final ClientVendorService clientVendorService;

    public ClientVendorController(ClientVendorService clientVendorService) {
        this.clientVendorService = clientVendorService;
    }

    @GetMapping("/list")
    public String listClientVendors(Model model) {
        model.addAttribute("clientVendors", clientVendorService.getAllClientVendors());
        return "clientVendor_list";
    }

    @GetMapping("/create")
    public String showCreateClientVendorForm(Model model) {
        model.addAttribute("clientVendor", new ClientVendorDTO());
        return "clientVendor_create";
    }

    @PostMapping("/create")
    public String createClientVendor(@ModelAttribute("clientVendor") ClientVendorDTO clientVendorDTO) {
        clientVendorService.createClientVendor(clientVendorDTO);
        return "redirect:/clientVendors/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateClientVendorForm(@PathVariable("id") Long id) {
         clientVendorService.findById(id);
        return "redirect:/clientVendors/list";
    }

    @PostMapping("/update/{id}")
    public String updateClientVendor(@PathVariable("id") Long id,
                                     @ModelAttribute("clientVendor") ClientVendorDTO clientVendorDTO) {
        clientVendorService.updateClientVendor(id, clientVendorDTO);
        return "redirect:/clientVendors/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteClientVendor(@PathVariable("id") Long id) {
        clientVendorService.deleteClientVendorById(id);
        return "redirect:/clientVendors/list";
    }
}
