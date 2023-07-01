package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.CategoryDTO;
import com.cydeo.accounting_app.dto.CompanyDTO;
import com.cydeo.accounting_app.entity.Category;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.CategoryRepository;
import com.cydeo.accounting_app.service.CategoryService;
import com.cydeo.accounting_app.service.LoggedInUserService;
import com.cydeo.accounting_app.service.ProductService;
import com.cydeo.accounting_app.service.SecurityService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends LoggedInUserService implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    public CategoryServiceImpl(SecurityService securityService, MapperUtil mapperUtil, CategoryRepository categoryRepository, ProductService productService) {
        super(securityService, mapperUtil);
        this.categoryRepository = categoryRepository;
        this.productService = productService;
    }

    @Override
    public CategoryDTO findById(Long id) {
        Category categoryInDb = categoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("This category doesn't exist"));
        CategoryDTO categoryDTO = mapperUtil.convert(categoryInDb, new CategoryDTO());
        categoryDTO.setHasProduct(hasProduct(categoryDTO.getId()));
        return categoryDTO;
    }

    /**
     * This is a service method to get all categories based on the current company
     *
     * @return Will return categories as dtoS
     */
    @Override
    public List<CategoryDTO> getCategoryList() {
        List<Category> categoryList = categoryRepository.findAllByCompany(getCompany());
        return categoryList.stream()
                .sorted(Comparator.comparing(Category::getDescription))
                .map(category -> mapperUtil.convert(category, new CategoryDTO()))
                .peek(categoryDTO -> categoryDTO.setHasProduct(hasProduct(categoryDTO.getId())))
                .collect(Collectors.toList());
    }

    /**
     * Get all products of this category. If this category has any product then return true
     *
     * @param categoryId is used for getting products of this category
     * @return boolean statement
     */
    private boolean hasProduct(Long categoryId) {
        return productService.findAllProductsWithCategoryId(categoryId).size() > 0;
    }

    @Override
    public boolean isCategoryDescriptionExist(CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findByDescriptionAndCompany(categoryDTO.getDescription(), getCompany());
        if (existingCategory == null) {
            return false;
        }
        return !existingCategory.getId().equals(categoryDTO.getId());
    }

    @Override
    public CategoryDTO create(CategoryDTO categoryDto) {
        Category category = mapperUtil.convert(categoryDto, new Category());
        category.setCompany(getCompany());
        return mapperUtil.convert(categoryRepository.save(category), new CategoryDTO());
    }

    @Override
    public CategoryDTO update(Long categoryId, CategoryDTO categoryDto) {
        Category categoryInDb = categoryRepository.findById(categoryId).orElseThrow(() -> new NoSuchElementException("This category doesn't exist"));
        categoryInDb.setDescription(categoryDto.getDescription());
        return mapperUtil.convert(categoryRepository.save(categoryInDb), new CategoryDTO());
    }

    @Override
    public void delete(Long categoryId) {
        Category categoryInDb = categoryRepository.findById(categoryId).orElseThrow(() -> new NoSuchElementException("This category doesn't exist"));
        categoryInDb.setIsDeleted(true);
        //We may use the same description in the future
        categoryInDb.setDescription(categoryInDb.getDescription() + "-" + categoryInDb.getId());
        categoryRepository.save(categoryInDb);
    }


}
