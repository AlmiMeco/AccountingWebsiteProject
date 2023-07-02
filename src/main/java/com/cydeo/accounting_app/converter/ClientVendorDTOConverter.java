package com.cydeo.accounting_app.converter;


import com.cydeo.accounting_app.dto.ClientVendorDTO;
import com.cydeo.accounting_app.service.ClientVendorService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClientVendorDTOConverter implements Converter<String, ClientVendorDTO> {
    private final ClientVendorService clientVendorDTO;
    public ClientVendorDTOConverter(ClientVendorService clientVendorDTO) {
        this.clientVendorDTO = clientVendorDTO;
    }
    @Override
    public ClientVendorDTO convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return clientVendorDTO.findById(Long.valueOf(source));
    }


}
