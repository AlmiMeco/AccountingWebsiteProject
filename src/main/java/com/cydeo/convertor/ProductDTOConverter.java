package com.cydeo.convertor;

import com.cydeo.accounting_app.dto.ProductDTO;
import com.cydeo.accounting_app.service.ProductService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductDTOConverter implements Converter<String, ProductDTO> {

    ProductService productService;

    public ProductDTOConverter(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ProductDTO convert(String source) {
        return productService.findById(Long.valueOf(source));
    }
}
