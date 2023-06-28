package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.RoleDTO;
import com.cydeo.accounting_app.entity.Role;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.RoleRepository;
import com.cydeo.accounting_app.service.RoleService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final MapperUtil mapper;

    public RoleServiceImpl(RoleRepository repository, MapperUtil mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public RoleDTO findById(Long id) {

        Role role = repository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("Role with given ID does not exist")
        );

        return mapper.convert(role, new RoleDTO());

    }

    @Override
    public List<RoleDTO> listAllRoles() {

        return repository.findAll().stream()
                .map(i -> mapper.convert(i, new RoleDTO()))
                .collect(Collectors.toList());
    }


}
