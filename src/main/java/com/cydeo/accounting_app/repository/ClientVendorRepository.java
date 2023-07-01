package com.cydeo.accounting_app.repository;

import com.cydeo.accounting_app.entity.ClientVendor;
import com.cydeo.accounting_app.entity.Company;
import com.cydeo.accounting_app.enums.ClientVendorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientVendorRepository extends JpaRepository<ClientVendor,Long> {
    @Query("SELECT cv " +
            "FROM ClientVendor cv " +
            "WHERE cv.company.id = ?1")
    List<ClientVendor> findAllByCompany(Long currentCompanyId);


    @Query("SELECT cv " +
            "FROM ClientVendor cv " +
            "WHERE cv.clientVendorType = ?1 " +
            "AND cv.company.id = ?2")
    List<ClientVendor> findClientVendorsByClientVendorTypeAndCompanyId(ClientVendorType type, Long companyId);

}