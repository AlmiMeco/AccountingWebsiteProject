package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.UserDTO;
import com.cydeo.accounting_app.repository.UserRepository;
import com.cydeo.accounting_app.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO findByUsername(String Username) {
        return null;
    }
}
