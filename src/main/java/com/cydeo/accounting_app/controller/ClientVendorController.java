package com.cydeo.accounting_app.controller;
import com.cydeo.accounting_app.dto.ClientVendorDTO;
import com.cydeo.accounting_app.service.ClientVendorService;
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
        return "/clientVendor/clientVendor-list";
    }

    @GetMapping("/create")
    public String showCreateClientVendorForm(Model model) {
        model.addAttribute("newClientVendor", new ClientVendorDTO());
        model.addAttribute("clientVendorTypes", clientVendorService.clientVendorType());
        model.addAttribute("countries", clientVendorService.listOfCountry());
        return "clientVendor/clientVendor-create";
    }

    @PostMapping("/create")
    public String createClientVendor(@ModelAttribute("newClientVendor") ClientVendorDTO clientVendorDTO, Model model) {
        clientVendorService.createClientVendor(clientVendorDTO);
        model.addAttribute("clientVendorTypes", clientVendorService.clientVendorType());
        return "redirect:/clientVendors/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateClientVendorForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("clientVendor", clientVendorService.findById(id));
        model.addAttribute("clientVendorTypes", clientVendorService.clientVendorType());
        model.addAttribute("countries", clientVendorService.listOfCountry());
        return "/clientVendor/clientVendor-update";
    }

    @PostMapping("/update/{id}")
    public String updateClientVendor(@PathVariable("id") Long id,
                                     @ModelAttribute("clientVendor") ClientVendorDTO clientVendorDTO,
                                     Model model) {
        model.addAttribute("clientVendor", clientVendorService.updateClientVendor(id, clientVendorDTO));
        model.addAttribute("clientVendorTypes", clientVendorService.clientVendorType());
        return "redirect:/clientVendors/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteClientVendor(@PathVariable("id") Long id) {
        clientVendorService.deleteClientVendorById(id);
        return "redirect:/clientVendors/list";
    }
}
