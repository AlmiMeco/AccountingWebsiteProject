package com.cydeo.accounting_app.converter;

import com.cydeo.accounting_app.dto.InvoiceDTO;
import com.cydeo.accounting_app.service.InvoiceService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class InvoiceDTOConverter implements Converter<String, InvoiceDTO> {

    private final InvoiceService invoiceService;

    public InvoiceDTOConverter(@Lazy InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Override
    public InvoiceDTO convert(String source) {
        if (source.isEmpty() || source.isBlank()) {return null;}
        return invoiceService.findById(Long.parseLong(source));
    }
}