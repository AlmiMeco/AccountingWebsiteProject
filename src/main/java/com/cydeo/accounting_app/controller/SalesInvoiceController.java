package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.dto.InvoiceDTO;
import com.cydeo.accounting_app.dto.InvoiceProductDTO;
import com.cydeo.accounting_app.enums.ClientVendorType;
import com.cydeo.accounting_app.enums.InvoiceType;
import com.cydeo.accounting_app.service.ClientVendorService;
import com.cydeo.accounting_app.service.InvoiceProductService;
import com.cydeo.accounting_app.service.InvoiceService;
import com.cydeo.accounting_app.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/salesInvoices")
public class SalesInvoiceController {

    private final InvoiceService invoiceService;
    private final ClientVendorService clientVendorService;
    private final InvoiceProductService invoiceProductService;
    private final ProductService productService;

    public SalesInvoiceController(InvoiceService invoiceService, ClientVendorService clientVendorService, InvoiceProductService invoiceProductService, ProductService productService) {
        this.invoiceService = invoiceService;
        this.clientVendorService = clientVendorService;
        this.invoiceProductService = invoiceProductService;
        this.productService = productService;
    }

    @GetMapping("/create")
    public String createInvoice(Model model){
        model.addAttribute("newSalesInvoice", invoiceService.createInvoice(InvoiceType.SALES));
        return "invoice/sales-invoice-create";
    }

    @PostMapping("/create")
    public String insertInvoice(@ModelAttribute("newSalesInvoice") InvoiceDTO invoiceDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("newSalesInvoice", invoiceService.createInvoice(InvoiceType.SALES));
            return "invoice/sales-invoice-create";
        }
        invoiceService.saveInvoiceByType(invoiceDTO,InvoiceType.SALES);
        String id = invoiceService.findLastInvoiceId();
        return "redirect:/salesInvoices/update/"+id;
    }

    @GetMapping("/list")
    public String listInvoices(Model model){
        return "invoice/sales-invoice-list";
    }

    @GetMapping("/update/{id}")
    public String updateInvoice(@PathVariable("id") Long id, Model model){
        model.addAttribute("invoice",invoiceService.findById(id));
        model.addAttribute("invoiceProducts", invoiceProductService.findAllInvoiceProductsByInvoiceId(id));
        model.addAttribute("newInvoiceProduct", new InvoiceProductDTO());
        return "invoice/sales-invoice-update";
    }

    @PostMapping("/update/{id}")
    public String insertInvoice(@ModelAttribute("newSalesInvoice")InvoiceDTO invoiceDTO,BindingResult bindingResult,
                                @PathVariable("id") Long id, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("newSalesInvoice", invoiceService.createInvoice(InvoiceType.SALES));
            return "invoice/sales-invoice-create";
        }
        return "redirect:/salesInvoices/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable("id") Long id) {
        invoiceService.deleteInvoiceById(id);
        return "redirect:/salesInvoices/list";
    }

    @GetMapping("/approve/{id}")
    public String approveInvoice(@PathVariable("id") Long id) {
        invoiceService.approveInvoiceById(id);
        return "redirect:/salesInvoices/list";
    }


    @GetMapping("/removeInvoiceProduct/{invoiceId}/{productId}")
    public String removeInvoice(@PathVariable("invoiceId") Long invoiceId,
                                @PathVariable("productId") Long productId){
        invoiceProductService.deleteInvoiceProductById(productId);
        return "redirect:/salesInvoices/update/"+invoiceId;
    }


    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public String insertInvoiceProduct(@ModelAttribute("newInvoiceProduct") InvoiceProductDTO invoiceProductDTO,
                                       BindingResult bindingResult, @PathVariable("invoiceId") Long invoiceId, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("invoice",invoiceService.findById(invoiceId));
            model.addAttribute("newInvoiceProduct", new InvoiceProductDTO());
            return "invoice/sales-invoice-update"+invoiceId;
        }
        invoiceProductService.saveInvoiceProduct(invoiceProductDTO,invoiceId);
        return "redirect:/salesInvoices/update/"+invoiceId;
    }

    @GetMapping("/print/{invoiceId}")
    public String removeInvoice(@PathVariable("invoiceId") Long invoiceId, Model model){
        model.addAttribute("invoice",invoiceService.findById(invoiceId));
        model.addAttribute("invoiceProducts", invoiceProductService.findAllInvoiceProductsByInvoiceId(invoiceId));
        model.addAttribute("company", invoiceService.getCurrentCompany());
        return "invoice/invoice_print";
    }

    @ModelAttribute
    public void commonModel(Model model){
        model.addAttribute("clients", clientVendorService.listAllClientVendorsByTypeAndCompany(ClientVendorType.CLIENT));
        model.addAttribute("invoices",invoiceService.listAllInvoicesByType(InvoiceType.SALES));
        model.addAttribute("products", productService.getProductList());
    }
}
