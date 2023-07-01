package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.ProductDTO;
import com.cydeo.accounting_app.entity.Product;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.ProductRepository;
import com.cydeo.accounting_app.service.LoggedInUserService;
import com.cydeo.accounting_app.service.ProductService;
import com.cydeo.accounting_app.service.SecurityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends LoggedInUserService implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(SecurityService securityService, MapperUtil mapperUtil, ProductRepository productRepository) {
        super(securityService, mapperUtil);
        this.productRepository = productRepository;
    }

    @Override
    public ProductDTO findById(Long id) {
        Optional<Product> byId = productRepository.findById(id);
        Product product = byId.orElseThrow( ()-> new NoSuchElementException("Product not found"));
        return mapperUtil.convert(product, new ProductDTO());
    }

    @Override
    public List<ProductDTO> getProductList() {
        List<Product> productList = productRepository.findAll();
        return productList.stream()
                .map(newProductList -> mapperUtil.convert(newProductList, new ProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> findAllProductsWithCategoryId(Long categoryId) {
        return productRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(product -> mapperUtil.convert(product, new ProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> findAllProductsByCompany() {
        return productRepository.findAllByCompanyId(getCompany().getId()).stream()
                .map(product -> mapperUtil.convert(product,new ProductDTO()))
                .collect(Collectors.toList());
    }
}
