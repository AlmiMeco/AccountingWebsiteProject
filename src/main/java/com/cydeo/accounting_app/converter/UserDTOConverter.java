package com.cydeo.accounting_app.converter;

import com.cydeo.accounting_app.dto.UserDTO;
import com.cydeo.accounting_app.service.UserService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class UserDTOConverter implements Converter<String , UserDTO> {
    private final UserService userService;

    public UserDTOConverter(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDTO convert(String source) {
        if (source == null || source.equals("")) {return null;}
        return userService.findById(Long.parseLong(source));
    }
}
