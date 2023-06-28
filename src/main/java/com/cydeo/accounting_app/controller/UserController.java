package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.dto.CompanyDTO;
import com.cydeo.accounting_app.dto.UserDTO;
import com.cydeo.accounting_app.service.RoleService;
import com.cydeo.accounting_app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping("/create")
    public String userCreate(Model model){

        model.addAttribute("newUser", new UserDTO());
        model.addAttribute("userRoles", roleService.listAllRoles());
        model.addAttribute("companies", List.of(new CompanyDTO()));

        return "user/user-create";
    }

    @GetMapping("/list")
    public String listUser(Model model){
        //        Not Working (requires UserService impl -> listAllUsers() method)
        model.addAttribute("users", userService.listAllUsers() );

        return "user/user-list";

    }

    @GetMapping("/update/{id}")
    public String editUser(@PathVariable("id") Long id, Model model){
        //        Not Working (requires UserService impl -> findById() method)
        model.addAttribute("users", userService.findById(id));

        return "user/user-update";

    }


}
