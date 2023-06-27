package com.cydeo.accounting_app.dto;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryDTO {

        private Long id;
        private String description;
        private CompanyDTO company;
        private boolean hasProduct;

}
