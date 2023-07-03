package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.UserDTO;
import com.cydeo.accounting_app.entity.Company;
import com.cydeo.accounting_app.entity.User;
import com.cydeo.accounting_app.mapper.MapperUtil;

import com.cydeo.accounting_app.repository.UserRepository;
import com.cydeo.accounting_app.service.LoggedInUserService;
import com.cydeo.accounting_app.service.SecurityService;
import com.cydeo.accounting_app.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends LoggedInUserService implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(SecurityService securityService, MapperUtil mapperUtil, UserRepository userRepository) {
        super(securityService, mapperUtil);
        this.userRepository = userRepository;
    }


    @Override
    public UserDTO findByUsername(String Username) {
        User user = userRepository.findByUsername(Username).orElseThrow(
                () -> new UsernameNotFoundException("This user does not exist"));
        return mapperUtil.convert(user,new UserDTO());
    }

    @Override
    public UserDTO findById(Long id) {
        var user = userRepository.getUserById(id);
        return mapperUtil.convert(user, new UserDTO());
    }

    @Override
    public List<UserDTO> listAllUsers() {

        Long loggedInUserCompanyId = getCompany().id;

        if (userRepository.getUserById(loggedInUserCompanyId).getRole().getId() == 1L) {

             List<User> adminUsers = userRepository.findAllByRoleId(2L);

             return adminUsers.stream()
                     .map(i -> mapperUtil.convert(i, new UserDTO()))
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
        user.setEnabled(true);
        userRepository.save(user);

    }

    @Override
    public void softDelete(Long id) {

        User user = userRepository.getUserById(id);
        user.setIsDeleted(true);
        user.setUsername(user.getUsername() + "-depreciatedUserName");
        userRepository.save(user);

    }

    @Override
    public void delete(Long id) {

        User user = userRepository.getUserById(id);
        userRepository.delete(user);

    }

    @Override
    public boolean isEmailAlreadyExisting(UserDTO userDTO) {

        User existingUser = userRepository.getUserById(userDTO.getId());

        if (existingUser == null) {return false;}

        return !existingUser.getUsername().equals(userDTO.getUsername());

    }

}
