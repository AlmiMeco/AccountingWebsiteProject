package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.CompanyDTO;
import com.cydeo.accounting_app.entity.Company;
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
    public CompanyDTO updateCompany(CompanyDTO companyDTO) {
        //Find current company
        Company company1 = companyRepository.findCompanyById(companyDTO.getId()).get();  //has id
        //Map update company dto to entity object
        Company convertedCompany = mapper.convert(company1, new Company());   // has id?
        //set id to the converted object
        convertedCompany.setId(company1.id);
        //save the updated company in the db
        companyRepository.save(convertedCompany);

        return findById(convertedCompany.id);
    }

    @Override
    public void deleteCompany(Long id) {

    }
}
