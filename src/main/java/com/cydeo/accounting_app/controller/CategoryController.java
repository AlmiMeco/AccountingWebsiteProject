package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.dto.CategoryDTO;
import com.cydeo.accounting_app.entity.Category;
import com.cydeo.accounting_app.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        public String createCategory(@ModelAttribute("newCategory") CategoryDTO categoryDTO, Model model) {

            return "redirect:/categories/list";
        }


        @GetMapping("/update/{id}")
        public String showUpdateCategory(@PathVariable("id") Long id, Model model) {

            return "/clientVendor/clientVendor-update";
        }



        @PostMapping("/update/{id}")
        public String updateClientVendor(@PathVariable("id") Long id,
                                         @ModelAttribute("clientVendor") CategoryDTO categoryDTO,
                                         Model model) {


            return "redirect:/categories/list";
        }



        @GetMapping("/delete/{id}")
        public String deleteCategory(@PathVariable("id") Long id) {
//        categoryService.deleteCategoryById(id);
            return "redirect:/categories/list";
        }
    }

