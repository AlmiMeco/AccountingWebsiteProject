package com.cydeo.accounting_app.service;

import com.cydeo.accounting_app.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO findById(Long id);

    List<CategoryDTO> getCategoryList();
}
