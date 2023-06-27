package com.cydeo.accounting_app.service;

import com.cydeo.accounting_app.dto.CategoryDTO;

public interface CategoryService {

    CategoryDTO findById(Long source);
}
