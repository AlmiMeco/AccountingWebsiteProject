package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.UserDTO;
import com.cydeo.accounting_app.entity.User;
import com.cydeo.accounting_app.mapper.MapperUtil;

import com.cydeo.accounting_app.repository.UserRepository;
import com.cydeo.accounting_app.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private MapperUtil mapper;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public UserDTO findByUsername(String Username) {
        User user = userRepository.findByUsername(Username).orElseThrow(
                () -> new UsernameNotFoundException("This user does not exist"));
        return mapper.convert(user,new UserDTO());
    }

    @Override
    public UserDTO findById(Long id) {
        var user = userRepository.findById(id);
        return mapper.convert(user, new UserDTO());
    }

    @Override
    public List<UserDTO> listAllUsers() {

        return userRepository.findAll().stream()
                .map(i -> mapper.convert(i, new UserDTO()))
                .collect(Collectors.toList());
    }

}
