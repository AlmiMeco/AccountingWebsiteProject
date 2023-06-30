package com.cydeo.accounting_app.repository;

import com.cydeo.accounting_app.entity.ClientVendor;
import com.cydeo.accounting_app.enums.ClientVendorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientVendorRepository extends JpaRepository<ClientVendor,Long> {
    List<ClientVendor> findAllByCompanyId(Long currentCompanyId);

    List<ClientVendor> findClientVendorsByClientVendorType(ClientVendorType type);

}