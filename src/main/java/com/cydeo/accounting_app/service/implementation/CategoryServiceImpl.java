package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.CategoryDTO;
import com.cydeo.accounting_app.entity.Category;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.CategoryRepository;
import com.cydeo.accounting_app.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final MapperUtil mapperUtil;

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(MapperUtil mapperUtil, CategoryRepository categoryRepository) {
        this.mapperUtil = mapperUtil;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public CategoryDTO findById(Long id) {
        Optional<Category> byId = categoryRepository.findById(id);
        Category category = byId.orElseThrow( ()-> new NoSuchElementException("Category not found"));
        return mapperUtil.convert(category, new CategoryDTO());
    }

    @Override
    public List<CategoryDTO> getCategoryList() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream()
                .map(newCategoryList -> mapperUtil.convert(newCategoryList, new CategoryDTO()))
                .collect(Collectors.toList());
    }


}
