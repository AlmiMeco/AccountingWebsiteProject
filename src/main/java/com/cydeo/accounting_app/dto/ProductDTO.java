package com.cydeo.accounting_app.dto;

import com.cydeo.accounting_app.enums.ProductUnit;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDTO {

    private Long id;
    private String name;
    private Integer quantityInStock;
    private Integer lowLimitAlert;
    private ProductUnit productUnit;
    private CategoryDTO category;

}
