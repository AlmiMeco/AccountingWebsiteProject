package com.cydeo.accounting_app.service;
import com.cydeo.accounting_app.dto.ClientVendorDTO;
import com.cydeo.accounting_app.enums.ClientVendorType;
import java.util.List;

public interface ClientVendorService {
    ClientVendorDTO findById(Long id);

    List<ClientVendorDTO> getAllClientVendors();

    List<ClientVendorType> clientVendorType();

    void deleteClientVendorById(Long id);

    ClientVendorDTO updateClientVendor(Long id, ClientVendorDTO clientVendorDTO);

    ClientVendorDTO createClientVendor(ClientVendorDTO clientVendorDTO);

    Object listOfCountry();

    List<ClientVendorDTO> listAllClientVendorsByTypeAndCompany(ClientVendorType type);
}