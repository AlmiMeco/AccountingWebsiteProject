package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.entity.Address;
import com.cydeo.accounting_app.enums.ClientVendorType;
import com.cydeo.accounting_app.repository.ClientVendorRepository;
import com.cydeo.accounting_app.dto.ClientVendorDTO;
import com.cydeo.accounting_app.entity.ClientVendor;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.service.ClientVendorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientVendorServiceImpl implements ClientVendorService {
    private final ClientVendorRepository clientVendorRepository;
    private final MapperUtil mapperUtil;

    public ClientVendorServiceImpl(ClientVendorRepository clientVendorRepository,
                                   MapperUtil mapperUtil) {
        this.clientVendorRepository = clientVendorRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public ClientVendorDTO findById(Long id) {
        // create own exception later logic ?
        Optional<ClientVendor> byId = clientVendorRepository.findById(id);
        ClientVendor clientVendor = byId.orElseThrow(() -> new NoSuchElementException("Client vendor not found"));
        return mapperUtil.convert(clientVendor, new ClientVendorDTO());
    }

    @Override
    public List<ClientVendorDTO> getAllClientVendors() {
        // who can see list of Vendor Logic?
        List<ClientVendor> allClientVendors = clientVendorRepository.findAll();
        return allClientVendors.stream()
                .map(client -> mapperUtil.convert(client, new ClientVendorDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public ClientVendorDTO createClientVendor(ClientVendorDTO clientVendorDTO) {
        // who will create ClientVendor
        ClientVendor convert = mapperUtil.convert(clientVendorDTO, new ClientVendor());
        ClientVendor save = clientVendorRepository.save(convert);
        return mapperUtil.convert(save, new ClientVendorDTO());
    }

    @Override
    public String listOfCountry() {
        // later i need inject Address repository and return Address list
        String country = new Address().getCountry();
        return country;
    }

    @Override
    public List<ClientVendorType> clientVendorType() {
        return List.of(ClientVendorType.values());
    }

    @Override
    public void deleteClientVendorById(Long id) {
        Optional<ClientVendor> optionalClientVendor = clientVendorRepository.findById(id);
        ClientVendor existingClientVendor = optionalClientVendor
                .orElseThrow(() -> new NoSuchElementException("Client vendor not found"));
        existingClientVendor.setIsDeleted(true);
        clientVendorRepository.save(existingClientVendor);
    }

    @Override
    public ClientVendorDTO updateClientVendor(Long id, ClientVendorDTO clientVendorDTO) {
        // need logic for Update the existing and exception later ?
        Optional<ClientVendor> byId = clientVendorRepository.findById(id);
        ClientVendor convert = mapperUtil.convert(clientVendorDTO, new ClientVendor());
        convert.setId(byId.get().id);
        convert.setAddress(byId.get().getAddress());
        ClientVendor save = clientVendorRepository.save(convert);
        return mapperUtil.convert(save, new ClientVendorDTO());
    }
}




