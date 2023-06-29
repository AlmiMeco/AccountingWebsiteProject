package com.cydeo.accounting_app.service;

import com.cydeo.accounting_app.entity.Company;
import com.cydeo.accounting_app.entity.User;
import com.cydeo.accounting_app.mapper.MapperUtil;
import org.springframework.stereotype.Service;

@Service
public class LoggedInUserService {
    protected final SecurityService securityService;
    protected final MapperUtil mapperUtil;

    public LoggedInUserService(SecurityService securityService, MapperUtil mapperUtil) {
        this.securityService = securityService;
        this.mapperUtil = mapperUtil;
    }

    protected Company getCompany(){
        return mapperUtil.convert(securityService.getLoggedInUser().getCompany(), new Company());
    }

    protected User getCurrentUser(){
        return mapperUtil.convert(securityService.getLoggedInUser(), new User());
    }
}
