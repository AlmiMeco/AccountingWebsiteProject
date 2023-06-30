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
    public String insertInvoice(@ModelAttribute("newPurchaseInvoice")InvoiceDTO invoiceDTO,BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("newPurchaseInvoice", invoiceService.createInvoice(InvoiceType.PURCHASE));
            return "invoice/purchase-invoice-create";
        }
        invoiceService.saveInvoiceByType(invoiceDTO,InvoiceType.PURCHASE);
        String id = invoiceService.findLastInvoiceId();
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
    public String insertInvoice(@ModelAttribute("newPurchaseInvoice")InvoiceDTO invoiceDTO,BindingResult bindingResult,
                                @PathVariable("invoiceId") Long invoiceId, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("newPurchaseInvoice", invoiceService.createInvoice(InvoiceType.PURCHASE));
            return "invoice/purchase-invoice-create";
        }
        return "redirect:/purchaseInvoices/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable("id") Long id) {
        invoiceService.deleteInvoiceById(id);
        return "redirect:/purchaseInvoices/list";
    }

    @GetMapping("/approve/{id}")
    public String approveInvoice(@PathVariable("id") Long id) {
        invoiceService.approveInvoiceById(id);
        return "redirect:/purchaseInvoices/list";
    }


    @GetMapping("/removeInvoiceProduct/{invoiceId}/{productId}")
    public String removeInvoice(@PathVariable("invoiceId") Long invoiceId,
                                @PathVariable("productId") Long productId){
        invoiceProductService.deleteInvoiceProductById(productId);
        return "redirect:/purchaseInvoices/update/"+invoiceId;
    }


    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public String insertInvoiceProduct(@ModelAttribute("newInvoiceProduct") InvoiceProductDTO invoiceProductDTO,
                                       BindingResult bindingResult, @PathVariable("invoiceId") Long invoiceId, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("invoice",invoiceService.findById(invoiceId));
            model.addAttribute("newInvoiceProduct", new InvoiceProductDTO());
            return "invoice/purchase-invoice-update"+invoiceId;
        }
        invoiceProductService.saveInvoiceProduct(invoiceProductDTO,invoiceId);
        return "redirect:/purchaseInvoices/update/"+invoiceId;
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
        model.addAttribute("vendors", clientVendorService.listAllClientVendorsByTypeAndCompany(ClientVendorType.VENDOR));
        model.addAttribute("invoices",invoiceService.listAllInvoicesByTypeAndCompany(InvoiceType.PURCHASE,invoiceService.getCurrentCompany().getId()));
        model.addAttribute("products", productService.getProductList());
    }

}
