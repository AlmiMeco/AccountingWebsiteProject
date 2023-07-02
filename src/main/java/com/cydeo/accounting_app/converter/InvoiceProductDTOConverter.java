package com.cydeo.accounting_app.converter;

import com.cydeo.accounting_app.dto.InvoiceProductDTO;
import com.cydeo.accounting_app.service.InvoiceProductService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class InvoiceProductDTOConverter implements Converter<String, InvoiceProductDTO> {

    private final InvoiceProductService invoiceProductService;

    public InvoiceProductDTOConverter(@Lazy InvoiceProductService invoiceProductService) {
        this.invoiceProductService = invoiceProductService;
    }

    @Override
    public InvoiceProductDTO convert(String source) {
        if (source.isEmpty() || source.isBlank()) {return null;}
        return invoiceProductService.findById(Long.parseLong(source));
    }
}
