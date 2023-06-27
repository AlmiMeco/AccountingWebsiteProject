package com.cydeo.accounting_app.dto;

import com.cydeo.accounting_app.enums.ClientVendorType;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClientVendorDTO {
        private Long id;
        private String clientVendorName;
        private String phone;
        private String website;
        private ClientVendorType clientVendorType;
        private AddressDTO address;
        private CompanyDTO company;
}
