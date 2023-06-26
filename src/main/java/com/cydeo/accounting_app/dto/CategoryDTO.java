package com.cydeo.accounting_app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryDTO {

    Long id;
    String description;
    CompanyDto company;
    boolean hasProduct;
}
