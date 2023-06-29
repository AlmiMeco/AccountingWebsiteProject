package com.cydeo.accounting_app.service.implementation;


import com.cydeo.accounting_app.dto.UserDTO;
import com.cydeo.accounting_app.entity.User;
import com.cydeo.accounting_app.entity.common.UserPrincipal;
import com.cydeo.accounting_app.repository.UserRepository;
import com.cydeo.accounting_app.service.LoggedInUserService;
import com.cydeo.accounting_app.service.SecurityService;
import com.cydeo.accounting_app.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityServiceImpl implements SecurityService {
    private final UserRepository userRepository;
    private final UserService userService;

    public SecurityServiceImpl(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("This user does not exist"));
        return new UserPrincipal(user);
    }

    @Override
    public UserDTO getLoggedInUser() {
        String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByUsername(loggedInUserName);
    }
}
