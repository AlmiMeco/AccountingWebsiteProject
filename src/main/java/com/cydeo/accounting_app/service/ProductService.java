package com.cydeo.accounting_app.service;


import com.cydeo.accounting_app.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    ProductDTO findById(Long id);

    List<ProductDTO> getProductList();

    List<ProductDTO>findAllProductsWithCategoryId(Long categoryId);

    boolean isProductNameExist(ProductDTO productDTO);

    ProductDTO save(ProductDTO productDTO);
    ProductDTO update(Long id, ProductDTO productDTO);
    void delete(Long id);
    boolean checkProductQuantity(Long id);


}
