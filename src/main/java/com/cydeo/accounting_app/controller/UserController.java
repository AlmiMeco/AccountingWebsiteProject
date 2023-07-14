package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.dto.CompanyDTO;
import com.cydeo.accounting_app.dto.UserDTO;
import com.cydeo.accounting_app.service.CompanyService;
import com.cydeo.accounting_app.service.RoleService;
import com.cydeo.accounting_app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final CompanyService companyService;

    public UserController(UserService userService, RoleService roleService, CompanyService companyService) {
        this.userService = userService;
        this.roleService = roleService;
        this.companyService = companyService;
    }


    @GetMapping("/create")
    public String userCreate(Model model){

        model.addAttribute("newUser", new UserDTO());

        return "user/user-create";
    }

    @PostMapping("/create")
    public String userCreatePost(@Valid @ModelAttribute("newUser") UserDTO newlyCreatedUser, BindingResult bindingResult){

        boolean emailAlreadyExists = userService.isEmailAlreadyExisting(newlyCreatedUser);

        if (emailAlreadyExists || bindingResult.hasErrors()) {
            if (emailAlreadyExists) {bindingResult.rejectValue("username"," ","A user with this email already exists");}
            return "user/user-create";
        }

        userService.save(newlyCreatedUser);

        return "redirect:/users/list";

    }

    @GetMapping("/list")
    public String listUser(Model model){

        model.addAttribute("users", userService.listAllUsers() );

        return "user/user-list";

    }

    @GetMapping("/delete/{id}")
    public String userDelete(@PathVariable("id") Long id){

        userService.softDelete(id);

        return "redirect:/users/list";

    }

    @GetMapping("/update/{id}")
    public String editUser(@PathVariable("id") Long id, Model model){

        model.addAttribute("user", userService.findById(id));

        return "user/user-update";

    }



    @PostMapping("/update/{id}")
    public String editUserPost(@Valid @ModelAttribute("user") UserDTO updatedUser, BindingResult bindingResult,
                               @PathVariable("id") Long id){

        updatedUser.setId(id);

        boolean emailAlreadyExists = userService.isEmailAlreadyExisting(updatedUser);

        if (emailAlreadyExists || bindingResult.hasErrors()) {
            if (emailAlreadyExists) {bindingResult.rejectValue("username"," ","A user with this email already exists");}
            return "user/user-update";
        }

        userService.save(updatedUser);

        return "redirect:/users/list";

    }


    @ModelAttribute
    public void commonAttributes(Model model){
        model.addAttribute("userRoles", roleService.listAllRoles());
        model.addAttribute("companies", companyService.listAllCompanies());
        model.addAttribute("title", "Cydeo Accounting-User");
    }


}
