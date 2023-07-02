package com.cydeo.accounting_app.dto;

import com.cydeo.accounting_app.enums.ClientVendorType;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClientVendorDTO {
        private Long id;
        @NotBlank (message = "Company Name is a required fields")
        @Size(min = 2, max = 50, message = "Company name should have 2-50 characters long.")
        private String clientVendorName;

        @Pattern(regexp = "^(\\d{10})|(([\\(]?([0-9]{3})[\\)]?)?[ \\.\\-]?([0-9]{3})[ \\.\\-]([0-9]{4}))$",
                message = "\"Phone Number is required field and may be in any valid phone number format.\"")
        private String phone;
        @Pattern(regexp = "^http(s{0,1})://[a-zA-Z0-9/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9/\\&\\?\\=\\-\\.\\~\\%]*"
        ,message = "Website should have a valid format")
        private String website;
        @NotNull(message = "Please select type")
        private ClientVendorType clientVendorType;
        @Valid
        private AddressDTO address;
        @Valid
        private CompanyDTO company;
        public boolean isHasInvoice;
}
