package com.cydeo.accounting_app.service;

import com.cydeo.accounting_app.dto.UserDTO;

public interface UserService {

    UserDTO findByUsername(String Username);
}
