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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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
    public String insertInvoice(@ModelAttribute("newSalesInvoice") @Valid InvoiceDTO invoiceDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "invoice/sales-invoice-create";
        }
        invoiceService.saveInvoiceByType(invoiceDTO,InvoiceType.SALES);
        String id = invoiceService.findLastInvoiceId(InvoiceType.SALES);
        return "redirect:/salesInvoices/update/"+id;
    }

    @GetMapping("/list")
    public String listInvoices(Model model){
        return "invoice/sales-invoice-list";
    }

    @GetMapping("/update/{invoiceId}")
    public String updateInvoice(@PathVariable("invoiceId") Long invoiceId, Model model){
        model.addAttribute("newInvoiceProduct", new InvoiceProductDTO());
        model.addAttribute("invoice",invoiceService.findById(invoiceId));
        model.addAttribute("invoiceProducts", invoiceProductService.findAllInvoiceProductsByInvoiceId(invoiceId));
        return "invoice/sales-invoice-update";
    }

    @PostMapping("/update/{invoiceId}")
    public String insertUpdatedInvoice(@ModelAttribute("newSalesInvoice") @Valid InvoiceDTO invoiceDTO,BindingResult bindingResult,
                                 @PathVariable("invoiceId") Long invoiceId){
        if (bindingResult.hasErrors()) {
            return "invoice/sales-invoice-update";
        }
        invoiceService.updateInvoice(invoiceId,invoiceDTO);
        return "redirect:/salesInvoices/list";
    }

    @GetMapping("/delete/{invoiceId}")
    public String deleteInvoice(@PathVariable("invoiceId") Long invoiceId) {
        invoiceService.deleteInvoiceById(invoiceId);
        return "redirect:/salesInvoices/list";
    }

    @GetMapping("/approve/{invoiceId}")
    public String approveInvoice(@PathVariable("invoiceId") Long invoiceId) {
        invoiceService.approveInvoiceById(invoiceId);
        return "redirect:/salesInvoices/list";
    }


    @GetMapping("/removeInvoiceProduct/{invoiceId}/{productId}")
    public String removeInvoiceProduct(@PathVariable("invoiceId") Long invoiceId,
                                @PathVariable("productId") Long productId){
        invoiceProductService.deleteInvoiceProductById(productId);
        return "redirect:/salesInvoices/update/"+invoiceId;
    }

    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public String insertInvoiceProduct(@ModelAttribute("newInvoiceProduct") @Valid InvoiceProductDTO invoiceProductDTO,
                                       BindingResult bindingResult, Model model, @PathVariable("invoiceId") Long invoiceId,
                                       RedirectAttributes redirectAttrs) {
        boolean stockNotEnough = invoiceProductService.isStockNotEnough(invoiceProductDTO,invoiceId);
        if (bindingResult.hasErrors()){
            model.addAttribute("invoice",invoiceService.findById(invoiceId));
            model.addAttribute("invoiceProducts", invoiceProductService.findAllInvoiceProductsByInvoiceId(invoiceId));
            return "invoice/sales-invoice-update";
        }
        if (stockNotEnough){
            redirectAttrs.addFlashAttribute("error", "Not enough " +
                    invoiceProductDTO.getProduct().getName() + " quantity to sell.");
        }else{
            invoiceProductService.saveInvoiceProduct(invoiceProductDTO,invoiceId);
            if(!invoiceProductService.productLowLimitAlert(invoiceProductDTO,invoiceId).isEmpty()){
                redirectAttrs.addFlashAttribute("error", "Stock of " +
                        invoiceProductService.productLowLimitAlert(invoiceProductDTO,invoiceId) + " decreased below low limit");
            }
        }
        return "redirect:/salesInvoices/update/"+invoiceId;
    }

    @GetMapping("/print/{invoiceId}")
    public String printInvoice(@PathVariable("invoiceId") Long invoiceId, Model model){
        model.addAttribute("invoice",invoiceService.getInvoiceForPrint(invoiceId));
        model.addAttribute("invoiceProducts", invoiceProductService.findAllInvoiceProductsByInvoiceId(invoiceId));
        return "invoice/invoice_print";
    }

    @ModelAttribute
    public void commonModel(Model model){
        model.addAttribute("clients", clientVendorService.listAllClientVendorsByTypeAndCompany(ClientVendorType.CLIENT));
        model.addAttribute("invoices",invoiceService.listAllInvoicesByType(InvoiceType.SALES));
        model.addAttribute("products", productService.findAllProductsByCompany());
        model.addAttribute("company", invoiceService.getCurrentCompany());
        model.addAttribute("title", "Cydeo Accounting-Sales");
    }

}
