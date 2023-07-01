package com.cydeo.accounting_app.repository;

import com.cydeo.accounting_app.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findCompanyById(Long id);

    List<Company> findAll();

    List<Company> findCompaniesByIdGreaterThanOrderByCompanyStatus(Long id);





}
