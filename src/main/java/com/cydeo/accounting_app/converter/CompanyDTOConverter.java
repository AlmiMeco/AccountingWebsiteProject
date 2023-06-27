package com.cydeo.accounting_app.converter;

import com.cydeo.accounting_app.dto.CompanyDTO;
import com.cydeo.accounting_app.service.CompanyService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;




@Component
@ConfigurationPropertiesBinding
public class CompanyDTOConverter implements Converter<String, CompanyDTO> {

    private final CompanyService companyService;

    public CompanyDTOConverter(@Lazy CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    public CompanyDTO convert(String source) {
        return companyService.findById(Long.parseLong(source));
    }
}
