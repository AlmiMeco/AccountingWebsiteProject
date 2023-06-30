package com.cydeo.accounting_app.repository;

import com.cydeo.accounting_app.entity.ClientVendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientVendorRepository extends JpaRepository<ClientVendor,Long> {
}
