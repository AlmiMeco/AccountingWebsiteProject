package com.cydeo.accounting_app.dto;

import com.cydeo.accounting_app.enums.CompanyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {

    private Long id;
    @NotNull()
    @NotBlank(message = "Product Name is a required field.")
    @Size(max = 100, min = 2, message = "Company Title should be 2-100 characters long.")
    private String title;
    private String phone;
    private String website;
    private AddressDTO address;
    private CompanyStatus companyStatus;
}
