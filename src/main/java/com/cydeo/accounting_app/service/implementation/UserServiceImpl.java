package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.UserDTO;
import com.cydeo.accounting_app.entity.Company;
import com.cydeo.accounting_app.entity.User;
import com.cydeo.accounting_app.exception.UserNotFoundException;
import com.cydeo.accounting_app.mapper.MapperUtil;

import com.cydeo.accounting_app.repository.UserRepository;
import com.cydeo.accounting_app.service.LoggedInUserService;
import com.cydeo.accounting_app.service.SecurityService;
import com.cydeo.accounting_app.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends LoggedInUserService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(SecurityService securityService, MapperUtil mapperUtil, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(securityService, mapperUtil);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDTO findByUsername(String Username) {
        User user = userRepository.findByUsername(Username).orElseThrow(
                () -> new UserNotFoundException("This user does not exist"));
        return mapperUtil.convert(user,new UserDTO());
    }

    @Override
    public UserDTO findById(Long id) {
//        var user = userRepository.getUserById(id);
        var user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("This user does not exist"));
        return mapperUtil.convert(user, new UserDTO());
    }

    @Override
    public List<UserDTO> listAllUsers() {

        Long loggedInUserCompanyId = getCompany().id;

        if (userRepository.getUserById(loggedInUserCompanyId).getRole().getId() == 1L) {

             List<User> adminUsers = userRepository.findAllByRoleId(2L);

             return adminUsers.stream()
                     .map(i -> mapperUtil.convert(i, new UserDTO()))
                     .peek(userDTO -> userDTO.setOnlyAdmin(checkIfOnlyAdminForCompany(userDTO)))
                     .collect(Collectors.toList());

        }

        List<User> allUsersBelongingToCompany = userRepository.findAllByCompanyId(loggedInUserCompanyId);

        return allUsersBelongingToCompany.stream()
                .sorted(Comparator.comparing(i -> {
                    Company company = i.getCompany();
                    return company.getCompanyStatus().name() + company.getTitle();
                } ))
                .map(i -> mapperUtil.convert(i, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(UserDTO userDTO) {

        User user = mapperUtil.convert(userDTO, new User());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);

    }

    @Override
    public void softDelete(Long id) {

        User user = userRepository.getUserById(id);
        user.setIsDeleted(true);
        user.setUsername(user.getUsername() + "-depreciatedUserName-" + user.getId());
        userRepository.save(user);

    }

    @Override
    public void delete(Long id) {

        User user = userRepository.getUserById(id);
        userRepository.delete(user);

    }

    @Override
    public boolean isEmailAlreadyExisting(UserDTO userDTO) {

        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());

        if (existingUser.isEmpty()) {return false;}

        return !existingUser.get().getId().equals(userDTO.getId());


    }

    @Override
    public UserDTO update(UserDTO dto) {

        User user = mapperUtil.convert(dto, new User());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(userRepository.findById(user.getId()).get().isEnabled());
        userRepository.save(user);

        return mapperUtil.convert(user, new UserDTO());
    }

    private Boolean checkIfOnlyAdminForCompany(UserDTO userDTO){

        return userRepository.countAllByCompanyAndRoleDescription(mapperUtil.convert(userDTO, new User()).getCompany(), "Admin") == 1;

    }

}
