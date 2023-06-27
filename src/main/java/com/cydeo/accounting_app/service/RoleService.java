package com.cydeo.accounting_app.service;

import com.cydeo.accounting_app.dto.RoleDTO;

import java.util.List;

public interface RoleService {

    RoleDTO findById(Long id);

    List<RoleDTO> listAllRoles();

}
