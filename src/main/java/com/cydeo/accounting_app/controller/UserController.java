package com.cydeo.accounting_app.controller;

import com.cydeo.accounting_app.dto.UserDTO;
import com.cydeo.accounting_app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/create")
    public String userCreate(Model model){

        model.addAttribute("user", new UserDTO());

        return "/user/user-create";
    }

    @GetMapping("/list")
    public String listUser(Model model){
        //        Not Working (requires UserService impl -> listAllUsers() method)
        model.addAttribute("users", userService.listAllUsers() );

        return "/user/user-list";

    }

    @GetMapping("/list/{id}")
    public String editUser(@PathVariable("id") Long id, Model model){
        //        Not Working (requires UserService impl -> findById() method)
        model.addAttribute("users", userService.findById(id));

        return "/user/user-update";

    }


}
