package com.cydeo.accounting_app.repository;

import com.cydeo.accounting_app.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository <Address, Long> {

    Optional<Address> findAllByCountry(String country);
    Optional<Address> findAllByState(String state);
    Optional<Address> findAllByCity(String city);
    Optional<Address> findAllByIsDeleted(boolean isDeleted);
    Optional<Address> findById(Long id);
    Optional<Address> findAddressByCountry(String country);
    Optional<Address> findAddressByState(String state);
    Optional<Address> findAddressByCity(String city);
    Optional<Address> findAddressById(Long id);

    Address getDistinctFirstByCity(String city);


}
