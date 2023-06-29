package com.cydeo.accounting_app.service;

import com.cydeo.accounting_app.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface SecurityService extends UserDetailsService {
    UserDTO getLoggedInUser();
}
