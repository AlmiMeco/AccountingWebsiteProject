package com.cydeo.accounting_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/templates")
public class TemplateController {

    @GetMapping
    public String getTemplate(){
        return "/template";
    }
}
