package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.CompanyDTO;
import com.cydeo.accounting_app.entity.Company;
import com.cydeo.accounting_app.enums.CompanyStatus;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.CompanyRepository;
import com.cydeo.accounting_app.service.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final MapperUtil mapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, MapperUtil mapper) {
        this.companyRepository = companyRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CompanyDTO> listAllCompanies() {
         return companyRepository.findAll().stream().
                 map(company -> mapper.convert(company, new CompanyDTO())).
                 collect(Collectors.toList());

    }

    @Override
    public CompanyDTO findById(Long id) {
        Company company = companyRepository.findCompanyById(id).
                orElseThrow(() -> new NoSuchElementException
                        ("Company with id# "+id+" is not found"));
        return mapper.convert(company, new CompanyDTO());
    }

    @Override
    public void updateCompany(Long id, CompanyDTO companyDTO) {

        Company company = mapper.convert(companyDTO, new Company());
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


    }

    @Override
    public void activateCompany(Long id) {

        Optional<Company> foundCompany = companyRepository.findById(id);

        if (foundCompany.isPresent()) {
            foundCompany.get().setIsDeleted(false);
            foundCompany.get().setCompanyStatus(CompanyStatus.ACTIVE);
            companyRepository.save(foundCompany.get());
        }
    }

        @Override
        public void deactivateCompany(Long id) {

            Optional<Company> foundCompany = companyRepository.findById(id);

            if(foundCompany.isPresent()) {
                foundCompany.get().setIsDeleted(true);
                foundCompany.get().setCompanyStatus(CompanyStatus.PASSIVE);
                companyRepository.save(foundCompany.get());
            }

    }

    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Company newCompany = mapper.convert(companyDTO, new Company());
        newCompany.setCompanyStatus(CompanyStatus.PASSIVE);
        return mapper.convert(companyRepository.save(newCompany), new CompanyDTO());
    }

    @Override
    public void saveCompany(CompanyDTO companyDTO) {
        if (companyDTO.getCompanyStatus()==null)
            companyDTO.setCompanyStatus(CompanyStatus.PASSIVE);
       companyRepository.save(mapper.convert(companyDTO, new Company()));



    }
}
