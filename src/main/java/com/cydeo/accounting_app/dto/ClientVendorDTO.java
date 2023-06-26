package com.cydeo.accounting_app.dto;

import com.sun.istack.NotNull;
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
        private AddressDto address;
        private CompanyDto company;
}
