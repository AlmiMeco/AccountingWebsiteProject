package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.CompanyDTO;
import com.cydeo.accounting_app.service.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CompanyServiceImpl implements CompanyService {
    @Override
    public List<CompanyDTO> listAllCompanies() {
        return null;
    }

    @Override
    public CompanyDTO findById(Long id) {
        return null;
    }
}
