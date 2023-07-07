package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.RoleDTO;
import com.cydeo.accounting_app.dto.UserDTO;
import com.cydeo.accounting_app.entity.Role;
import com.cydeo.accounting_app.entity.User;
import com.cydeo.accounting_app.exception.RoleNotFoundException;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.RoleRepository;
import com.cydeo.accounting_app.repository.UserRepository;
import com.cydeo.accounting_app.service.LoggedInUserService;
import com.cydeo.accounting_app.service.RoleService;
import com.cydeo.accounting_app.service.SecurityService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends LoggedInUserService implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleServiceImpl(SecurityService securityService, MapperUtil mapperUtil, RoleRepository roleRepository, UserRepository userRepository) {
        super(securityService, mapperUtil);
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }


    @Override
    public RoleDTO findById(Long id) {

        Role role = roleRepository.findById(id).orElseThrow(
                () -> new RoleNotFoundException("Role with given ID does not exist")
        );

        return mapperUtil.convert(role, new RoleDTO());

    }

    @Override
    public List<RoleDTO> listAllRoles() {

        User currentLoggedInUser = userRepository.getUserById(getCurrentUser().id);

        if (currentLoggedInUser.getId() == 1L) {

            return roleRepository.findById(2L).stream()
                    .map(i -> mapperUtil.convert(i, new RoleDTO()))
                    .collect(Collectors.toList());

        }else return roleRepository.findAll()
                .stream()
                .filter(i -> i.getId() >= 2L)
                .map(i -> mapperUtil.convert(i, new RoleDTO()))
                .collect(Collectors.toList());

    }


}
