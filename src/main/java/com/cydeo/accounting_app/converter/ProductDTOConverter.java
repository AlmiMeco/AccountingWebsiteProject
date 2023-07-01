package com.cydeo.accounting_app.converter;

import com.cydeo.accounting_app.dto.ProductDTO;
import com.cydeo.accounting_app.service.ProductService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductDTOConverter implements Converter<String, ProductDTO> {

    private final ProductService productService;

    public ProductDTOConverter(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ProductDTO convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return productService.findById(Long.valueOf(source));
    }
}
