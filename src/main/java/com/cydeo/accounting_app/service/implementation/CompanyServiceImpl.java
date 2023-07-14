package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.CompanyDTO;


import com.cydeo.accounting_app.entity.Company;
import com.cydeo.accounting_app.entity.User;
import com.cydeo.accounting_app.enums.CompanyStatus;
import com.cydeo.accounting_app.exception.CompanyNotFoundException;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.CompanyRepository;
import com.cydeo.accounting_app.repository.UserRepository;
import com.cydeo.accounting_app.service.AddressService;
import com.cydeo.accounting_app.service.CompanyService;
import com.cydeo.accounting_app.service.LoggedInUserService;
import com.cydeo.accounting_app.service.SecurityService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl extends LoggedInUserService implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final AddressService addressService;


    public CompanyServiceImpl(SecurityService securityService, MapperUtil mapperUtil, CompanyRepository companyRepository, UserRepository userRepository, AddressService addressService) {
        super(securityService, mapperUtil);
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;

        this.addressService = addressService;
    }


    @Override
    public List<CompanyDTO> listAllCompanies() {

        User currentLoggedInUser = userRepository.getUserById(getCurrentUser().id);
        Long currentLoggedInUserCompany = currentLoggedInUser.getCompany().id;

        if (currentLoggedInUser.getId() == 1L) {

            return companyRepository.findAll()
                    .stream()
                    .filter(i -> i.getId() >= 2L)
                    .map(i -> mapperUtil.convert(i, new CompanyDTO()))
                    .collect(Collectors.toList());

        }else return companyRepository.findCompanyById(currentLoggedInUserCompany)
                .stream()
                .map(i -> mapperUtil.convert(i, new CompanyDTO()))
                .collect(Collectors.toList());


    }

    @Override
    public List<CompanyDTO> listAllNonProviderCompanies() {
        List<CompanyDTO> list = mapperUtil.convert(companyRepository.findCompaniesByIdGreaterThanOrderByCompanyStatus(1L), new ArrayList<>());
        return list;
    }

    @Override
    public CompanyDTO findById(Long id) {
        Company company = companyRepository.findCompanyById(id).
                orElseThrow(() -> new CompanyNotFoundException(
                        ("Company with id# "+id+" is not found")));
        return mapperUtil.convert(company, new CompanyDTO());
    }

    @Override
    public CompanyDTO updateCompany(Long id, CompanyDTO companyDTO) {

        Company company = mapperUtil.convert(companyDTO, new Company());
        companyRepository.findCompanyById(id).ifPresent(company1 -> {
            company1.setId(company.getId());
            company1.setIsDeleted(company.getIsDeleted());
            company1.setTitle(company.getTitle());
            company1.setPhone(company.getPhone());
            company1.setAddress(company.getAddress());
            company1.setWebsite(company.getWebsite());
            Optional <Company> updatedCompany = companyRepository.findCompanyById(id);
            companyRepository.save(updatedCompany.orElseThrow());

        });

        return mapperUtil.convert(companyRepository.findCompanyById(id), new CompanyDTO());
    }

    @Override
    public void activateCompany(Long id) {

        Company foundCompany = companyRepository.findById(id).orElseThrow(() -> new CompanyNotFoundException("No Company with id#" +id+" found"));
            foundCompany.setCompanyStatus(CompanyStatus.ACTIVE);
          companyRepository.save(foundCompany);


        }


        @Override
        public void deactivateCompany(Long id) {
            Company foundCompany = companyRepository.findById(id).orElseThrow(() -> new CompanyNotFoundException("No Company with id#" +id+" found"));
            foundCompany.setCompanyStatus(CompanyStatus.PASSIVE);
           companyRepository.save(foundCompany);



            }

    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Company newCompany = mapperUtil.convert(companyDTO, new Company());
        newCompany.setCompanyStatus(CompanyStatus.PASSIVE);
        return mapperUtil.convert(companyRepository.save(newCompany), new CompanyDTO());
    }

    @Override
    public void saveCompany(CompanyDTO companyDTO) {
        if (companyDTO.getCompanyStatus()==null)
            companyDTO.setCompanyStatus(CompanyStatus.PASSIVE);
       companyRepository.save(mapperUtil.convert(companyDTO, new Company()));



    }

    @Override
    public boolean companyNameIsExist(CompanyDTO companyDTO) {
        Company existingCompany = companyRepository.findCompanyByTitle(companyDTO.getTitle());
        if (existingCompany == null) {
            return false;
        }
        return !existingCompany.getId().equals(companyDTO.getId());
    }

    @Override
    public List<String> getListOfCountries() {
        return addressService.listOfCountries();
    }

}
