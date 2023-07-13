package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.dto.ProductDTO;
import com.cydeo.accounting_app.entity.Product;
import com.cydeo.accounting_app.enums.ProductUnit;
import com.cydeo.accounting_app.service.CategoryService;
import com.cydeo.accounting_app.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String displayAllProducts() {
        return "/product/product-list";
    }

    @GetMapping("/create")
    public String openCreatePage(Model model){
        model.addAttribute("newProduct", new ProductDTO());
        return "/product/product-create";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute("newProduct") ProductDTO productDTO, BindingResult bindingResult){

        if (productService.isProductNameExist(productDTO)){
            bindingResult.rejectValue("name", " ", "This Product Name already exists.");
        }

        if (bindingResult.hasErrors()){
            return "/product/product-create";
        }
        productService.save(productDTO);
        return "redirect:/products/list";

    }

    @GetMapping("/update/{productId}")
    public String openUpdatePage(@PathVariable Long productId, Model model){
        model.addAttribute("product", productService.findById(productId));
        return "/product/product-update";
    }

    @PostMapping("/update/{productId}")
    public String updateProduct(@Valid @ModelAttribute("product") ProductDTO productDTO, BindingResult bindingResult, @PathVariable Long productId){
        productDTO.setId(productId);

        if (productService.isProductNameExist(productDTO)){
            bindingResult.rejectValue("name", " ", "This Product Name already exists.");
        }

        if (bindingResult.hasErrors()){
            return "/product/product-update";
        }
        productService.update(productId, productDTO);
        return "redirect:/products/list";
    }

    @GetMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable Long productId, RedirectAttributes redirectAttributes){

        if (productService.checkProductQuantity(productId)){
            redirectAttributes.addFlashAttribute("error","This product can not be deleted..." );
            return "redirect:/products/list";
        }
        productService.delete(productId);
        return "redirect:/products/list";
    }

    @ModelAttribute
    public void commonAttributes(Model model){
        model.addAttribute("products", productService.getProductList());
        model.addAttribute("categories", categoryService.getCategoryList());
        model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));
        model.addAttribute("title", "Cydeo Accounting-Product");
    }

}
