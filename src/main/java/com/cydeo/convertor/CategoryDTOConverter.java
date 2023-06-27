package com.cydeo.convertor;

import com.cydeo.accounting_app.dto.CategoryDTO;
import com.cydeo.accounting_app.service.CategoryService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.persistence.Column;

@Component
public class CategoryDTOConverter implements Converter<String, CategoryDTO> {

    CategoryService categoryService;

    public CategoryDTOConverter(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public CategoryDTO convert(String source) {
        return categoryService.findById(Long.valueOf(source));
    }
}
