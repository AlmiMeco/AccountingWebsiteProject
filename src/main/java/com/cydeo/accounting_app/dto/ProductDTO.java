package com.cydeo.accounting_app.dto;

import com.cydeo.accounting_app.enums.ProductUnit;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDTO {

    Long id;
    String name;
    Integer quantityInStock;
    Integer lowLimitAlert;
    ProductUnit productUnit;
    CategoryDTO category;

}
