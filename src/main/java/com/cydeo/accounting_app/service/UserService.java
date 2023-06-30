package com.cydeo.accounting_app.service;

import com.cydeo.accounting_app.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO findByUsername(String Username);

    UserDTO findById(Long id);

    List<UserDTO> listAllUsers();

    void save(UserDTO userDTO);

    void softDelete(Long id);

    void delete(Long id);

    boolean isEmailAlreadyExisting(UserDTO userDTO);

}
