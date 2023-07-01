package com.cydeo.accounting_app.service;


import com.cydeo.accounting_app.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    ProductDTO findById(Long id);

    List<ProductDTO> getProductList();

    List<ProductDTO>findAllProductsWithCategoryId(Long categoryId);

    List<ProductDTO> findAllProductsByCompany();


}
