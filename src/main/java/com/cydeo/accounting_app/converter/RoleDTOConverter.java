package com.cydeo.accounting_app.converter;

import com.cydeo.accounting_app.dto.RoleDTO;
import com.cydeo.accounting_app.service.RoleService;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;

public class RoleDTOConverter implements Converter<String, RoleDTO> {


    private final RoleService roleService;

    public RoleDTOConverter(@Lazy RoleService roleService) {
        this.roleService = roleService;
    }


    @Override
    public RoleDTO convert(String source) {
        return roleService.findById(Long.parseLong(source));
    }


}
