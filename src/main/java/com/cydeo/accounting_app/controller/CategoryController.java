package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.dto.CategoryDTO;
import com.cydeo.accounting_app.entity.Category;
import com.cydeo.accounting_app.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String displayAllCategory(Model model) {
        model.addAttribute("categories", categoryService.getCategoryList());
        return "/category/category-list";
    }

    @GetMapping("/create")
    public String createCategory(Model model) {
        model.addAttribute("newCategory", new Category());
        return "/category/category-create";
    }

    @PostMapping("/create")
    public String createCategory(@Valid @ModelAttribute("newCategory") CategoryDTO categoryDTO, BindingResult bindingResult) {
        boolean categoryDescriptionExist = categoryService.isCategoryDescriptionExist(categoryDTO);

        if (categoryDescriptionExist){
            bindingResult.rejectValue("description"," ","This category description already exists");
        }

        if (bindingResult.hasErrors()){
            return "/category/category-create";
        }
        categoryService.create(categoryDTO);
        return "redirect:/categories/list";
    }


    @GetMapping("/update/{categoryId}")
    public String showUpdateCategory(@PathVariable("categoryId") Long categoryId, Model model) {
        model.addAttribute("category", categoryService.findById(categoryId));
        return "/category/category-update";
    }


    @PostMapping("/update/{categoryId}")
    public String updateClientVendor(@Valid @ModelAttribute("category") CategoryDTO categoryDTO, BindingResult bindingResult,@PathVariable("categoryId") Long categoryId) {
        categoryDTO.setId(categoryId);
        boolean categoryDescriptionExist = categoryService.isCategoryDescriptionExist(categoryDTO);

        if (categoryDescriptionExist) {
            bindingResult.rejectValue("description", " ", "This category description already exists");
        }

        if (bindingResult.hasErrors()) {
            return "/category/category-update";
        }

        categoryService.update(categoryId, categoryDTO);

        return "redirect:/categories/list";
    }


    @GetMapping("/delete/{categoryId}")
    public String delete(@PathVariable("categoryId") Long categoryId) {
        categoryService.delete(categoryId);
        return "redirect:/categories/list";
    }

    @ModelAttribute
    public void commonAttribute(Model model){
        model.addAttribute("title", "Cydeo Accounting-Category");
    }
}

