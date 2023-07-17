package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.dto.InvoiceDTO;
import com.cydeo.accounting_app.dto.InvoiceProductDTO;
import com.cydeo.accounting_app.enums.ClientVendorType;
import com.cydeo.accounting_app.enums.InvoiceType;
import com.cydeo.accounting_app.exception.InvoiceProductNotFoundException;
import com.cydeo.accounting_app.service.ClientVendorService;
import com.cydeo.accounting_app.service.InvoiceProductService;
import com.cydeo.accounting_app.service.InvoiceService;
import com.cydeo.accounting_app.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Controller
@RequestMapping("/purchaseInvoices")
public class PurchasesInvoiceController {

    private final InvoiceService invoiceService;
    private final ClientVendorService clientVendorService;
    private final InvoiceProductService invoiceProductService;
    private final ProductService productService;

    public PurchasesInvoiceController(InvoiceService invoiceService, ClientVendorService clientVendorService, InvoiceProductService invoiceProductService, ProductService productService) {
        this.invoiceService = invoiceService;
        this.clientVendorService = clientVendorService;
        this.invoiceProductService = invoiceProductService;
        this.productService = productService;
    }

    @GetMapping("/create")
    public String createInvoice(Model model){
        model.addAttribute("newPurchaseInvoice", invoiceService.createInvoice(InvoiceType.PURCHASE));
        return "invoice/purchase-invoice-create";
    }

    @PostMapping("/create")
    public String insertInvoice(@ModelAttribute("newPurchaseInvoice") @Valid InvoiceDTO invoiceDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "invoice/purchase-invoice-create";
        }
        invoiceService.saveInvoiceByType(invoiceDTO,InvoiceType.PURCHASE);
        String id = invoiceService.findLastInvoiceId(InvoiceType.PURCHASE);
        return "redirect:/purchaseInvoices/update/"+id;
    }

    @GetMapping("/list")
    public String listInvoices(Model model){
        return "invoice/purchase-invoice-list";
    }

    @GetMapping("/update/{invoiceId}")
    public String updateInvoice(@PathVariable("invoiceId") Long invoiceId, Model model){
        model.addAttribute("invoice",invoiceService.findById(invoiceId));
        model.addAttribute("invoiceProducts", invoiceProductService.findAllInvoiceProductsByInvoiceId(invoiceId));
        model.addAttribute("newInvoiceProduct", new InvoiceProductDTO());
        return "invoice/purchase-invoice-update";
    }

    @PostMapping("/update/{invoiceId}")
    public String insertUpdatedInvoice(@ModelAttribute("newPurchaseInvoice") @Valid InvoiceDTO invoiceDTO,
                                 BindingResult bindingResult, @PathVariable("invoiceId") Long invoiceId){
        if (bindingResult.hasErrors()) {
            return "invoice/purchase-invoice-update";
        }
        invoiceService.updateInvoice(invoiceId,invoiceDTO);
        return "redirect:/purchaseInvoices/list";
    }

    @GetMapping("/delete/{invoiceId}")
    public String deleteInvoice(@PathVariable("invoiceId") Long invoiceId) {
        invoiceService.deleteInvoiceById(invoiceId);
        return "redirect:/purchaseInvoices/list";
    }

    @GetMapping("/approve/{invoiceId}")
    public String approveInvoice(@PathVariable("invoiceId") Long invoiceId) {
        invoiceService.approveInvoiceById(invoiceId);
        return "redirect:/purchaseInvoices/list";
    }

    @GetMapping("/removeInvoiceProduct/{invoiceId}/{productId}")
    public String removeInvoiceProduct(@PathVariable("invoiceId") Long invoiceId,
                                @PathVariable("productId") Long productId){
        invoiceProductService.deleteInvoiceProductById(productId);
        return "redirect:/purchaseInvoices/update/"+invoiceId;
    }

    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public String insertInvoiceProduct(@ModelAttribute("newInvoiceProduct") @Valid InvoiceProductDTO invoiceProductDTO,
                                       BindingResult bindingResult, Model model, @PathVariable("invoiceId") Long invoiceId) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("invoice",invoiceService.findById(invoiceId));
            model.addAttribute("invoiceProducts", invoiceProductService.findAllInvoiceProductsByInvoiceId(invoiceId));
            return "invoice/purchase-invoice-update";
        }
        invoiceProductService.saveInvoiceProduct(invoiceProductDTO,invoiceId);
        return "redirect:/purchaseInvoices/update/"+invoiceId;
    }

    @GetMapping("/print/{invoiceId}")
    public String printInvoice(@PathVariable("invoiceId") Long invoiceId, Model model){
        model.addAttribute("invoice",invoiceService.getInvoiceForPrint(invoiceId));
        model.addAttribute("invoiceProducts", invoiceProductService.findAllInvoiceProductsByInvoiceId(invoiceId));
        return "invoice/invoice_print";
    }

    @ModelAttribute
    public void commonModel(Model model){
        model.addAttribute("vendors", clientVendorService.listAllClientVendorsByTypeAndCompany(ClientVendorType.VENDOR));
        model.addAttribute("invoices",invoiceService.listAllInvoicesByType(InvoiceType.PURCHASE));
        model.addAttribute("products", productService.findAllProductsByCompany());
        model.addAttribute("company", invoiceService.getCurrentCompany());
        model.addAttribute("title", "Cydeo Accounting-Purchase");
    }
}
