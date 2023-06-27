package com.cydeo.accounting_app.service;

import com.cydeo.accounting_app.dto.ClientVendorDTO;

import java.util.List;

public interface ClientVendorService {
    ClientVendorDTO findById(Long id);
    List<ClientVendorDTO> getAllClientVendors();
    ClientVendorDTO deleteClientVendorById(Long id);
    ClientVendorDTO updateClientVendor(Long id, ClientVendorDTO clientVendorDTO);
    ClientVendorDTO createClientVendor(ClientVendorDTO clientVendorDTO);


}