package com.cydeo.accounting_app.service;

import com.cydeo.accounting_app.dto.CompanyDTO;

import java.util.List;

public interface CompanyService {

    List<CompanyDTO> listAllCompanies();

    List<CompanyDTO> listAllNonProviderCompanies();
    CompanyDTO findById(Long id);

    CompanyDTO updateCompany (Long id, CompanyDTO companyDTO);

    void activateCompany(Long id);

    void deactivateCompany(Long id);

    CompanyDTO createCompany (CompanyDTO companyDTO);

    void saveCompany(CompanyDTO companyDTO);

    boolean companyNameIsExist(CompanyDTO companyDTO);

    List<String> getListOfCountries();

}


