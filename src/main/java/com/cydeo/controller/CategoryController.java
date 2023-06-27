package com.cydeo.controller;

import com.cydeo.accounting_app.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/category_list")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String displayAllCategory(Model model){
        model.addAttribute("categoryList", categoryService.getCategoryList());
        return "/category-list.html";
    }


}
