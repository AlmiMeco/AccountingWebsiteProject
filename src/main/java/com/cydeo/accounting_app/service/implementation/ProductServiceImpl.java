package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.ProductDTO;
import com.cydeo.accounting_app.entity.Product;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.ProductRepository;
import com.cydeo.accounting_app.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final MapperUtil mapperUtil;
    private final ProductRepository productRepository;

    public ProductServiceImpl(MapperUtil mapperUtil, ProductRepository productRepository) {
        this.mapperUtil = mapperUtil;
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
}
