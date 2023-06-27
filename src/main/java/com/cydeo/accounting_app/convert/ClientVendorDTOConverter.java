package com.cydeo.accounting_app.convert;

import com.cydeo.accounting_app.dto.ClientVendorDTO;
import com.cydeo.accounting_app.service.ClientVendorService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClientVendorDTOConverter implements Converter<String, ClientVendorDTO> {
    ClientVendorService clientVendorDTO;
    public ClientVendorDTOConverter(ClientVendorService clientVendorDTO) {
        this.clientVendorDTO = clientVendorDTO;
    }
    @Override
    public ClientVendorDTO convert(String source) {
        return clientVendorDTO.findById(Long.valueOf(source));
    }


}
