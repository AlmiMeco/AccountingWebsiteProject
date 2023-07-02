package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.entity.Address;
import com.cydeo.accounting_app.entity.Company;
import com.cydeo.accounting_app.entity.User;
import com.cydeo.accounting_app.enums.ClientVendorType;
import com.cydeo.accounting_app.repository.AddressRepository;
import com.cydeo.accounting_app.repository.ClientVendorRepository;
import com.cydeo.accounting_app.dto.ClientVendorDTO;
import com.cydeo.accounting_app.entity.ClientVendor;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.service.ClientVendorService;
import com.cydeo.accounting_app.service.LoggedInUserService;
import com.cydeo.accounting_app.service.SecurityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientVendorServiceImpl extends LoggedInUserService implements ClientVendorService {
    private final ClientVendorRepository clientVendorRepository;
    private final AddressRepository addressRepository;

    public ClientVendorServiceImpl(SecurityService securityService, MapperUtil mapperUtil, ClientVendorRepository clientVendorRepository, AddressRepository addressRepository) {
        super(securityService, mapperUtil);
        this.clientVendorRepository = clientVendorRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    protected User getCurrentUser() {
        return super.getCurrentUser();
    }

    @Override
    protected Company getCompany() {
        return super.getCompany();
    }

    @Override
    public ClientVendorDTO findById(Long id) {
        // create own exception later logic ?
        Optional<ClientVendor> byId = clientVendorRepository.findById(id);
        ClientVendor clientVendor = byId.orElseThrow(() -> new NoSuchElementException("Client vendor not found " + id));
        return mapperUtil.convert(clientVendor, new ClientVendorDTO());
    }

    @Override
    public List<ClientVendorDTO> getAllClientVendors() {
        Long currentCompanyId = getCompany().getId();
        List<ClientVendor> allClientVendors = clientVendorRepository.findAllByCompany(currentCompanyId);
        return allClientVendors.stream()
                .map(clientVendor -> mapperUtil.convert(clientVendor, new ClientVendorDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public ClientVendorDTO createClientVendor(ClientVendorDTO clientVendorDTO) {
        // who will create ClientVendor
        ClientVendor convert = mapperUtil.convert(clientVendorDTO, new ClientVendor());
        if (convert.getCompany() == null) {
            convert.setCompany(getCompany());
            convert.setAddress(addressRepository.save(getCurrentUser().getCompany().getAddress()));
        }
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
    public List<ClientVendorDTO> listAllClientVendorsByTypeAndCompany(ClientVendorType type) {
        return clientVendorRepository.findClientVendorsByClientVendorTypeAndCompanyId(type, getCompany().id).stream()
                .map(clientVendor -> mapperUtil.convert(clientVendor, new ClientVendorDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClientVendorType> clientVendorType() {
        return List.of(ClientVendorType.values());
    }

    @Override
    public void deleteClientVendorById(Long id) {
        Optional<ClientVendor> optionalClientVendor = clientVendorRepository.findById(id);
        ClientVendor existingClientVendor = optionalClientVendor
                .orElseThrow(() -> new NoSuchElementException("Client vendor not found " + id));
        existingClientVendor.setIsDeleted(true);
        clientVendorRepository.save(existingClientVendor);
    }

    @Override
    public ClientVendorDTO updateClientVendor(Long id, ClientVendorDTO clientVendorDTO) {
        // need logic for Update the existing and exception later ?
        Optional<ClientVendor> byId = clientVendorRepository.findById(id);
        ClientVendor convert = mapperUtil.convert(clientVendorDTO, new ClientVendor());
        convert.setId(byId.get().id);
        convert.setCompany(byId.get().getCompany());
        convert.setAddress(addressRepository.save(byId.get().getAddress()));
        ClientVendor save = clientVendorRepository.save(convert);
        return mapperUtil.convert(save, new ClientVendorDTO());
    }

}




