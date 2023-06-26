package com.cydeo.accounting_app.service.implementation;


import com.cydeo.accounting_app.entity.User;
import com.cydeo.accounting_app.entity.common.UserPrincipal;
import com.cydeo.accounting_app.repository.UserRepository;
import com.cydeo.accounting_app.service.SecurityService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {
    private final UserRepository userRepository;

    public SecurityServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("This user does not exist");
        }
        return new UserPrincipal(user);

    }

}
