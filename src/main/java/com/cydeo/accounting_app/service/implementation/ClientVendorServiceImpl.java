package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.entity.Company;
import com.cydeo.accounting_app.entity.User;
import com.cydeo.accounting_app.enums.ClientVendorType;
import com.cydeo.accounting_app.exception.ClientVendorNotFoundException;
import com.cydeo.accounting_app.repository.AddressRepository;
import com.cydeo.accounting_app.repository.ClientVendorRepository;
import com.cydeo.accounting_app.dto.ClientVendorDTO;
import com.cydeo.accounting_app.entity.ClientVendor;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.service.ClientVendorService;
import com.cydeo.accounting_app.service.LoggedInUserService;
import com.cydeo.accounting_app.service.SecurityService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import java.util.stream.Collectors;

@Service
public class ClientVendorServiceImpl extends LoggedInUserService implements ClientVendorService {
    private final ClientVendorRepository clientVendorRepository;
    private final AddressRepository addressRepository;

    public ClientVendorServiceImpl(SecurityService securityService,
                                   MapperUtil mapperUtil,
                                   ClientVendorRepository clientVendorRepository,
                                   AddressRepository addressRepository) {
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
        ClientVendor clientVendor = clientVendorRepository.findById(id)
                .orElseThrow(() -> new ClientVendorNotFoundException("Client vendor not found " + id));
        return mapperUtil.convert(clientVendor, new ClientVendorDTO());
    }

    @Override
    public List<ClientVendorDTO> getAllClientVendors() {
        List<ClientVendor> allClientVendors = clientVendorRepository.findByCompany(getCompany());
        return allClientVendors.stream()
                .sorted(Comparator.comparing(ClientVendor::getClientVendorType)
                        .reversed().thenComparing(ClientVendor::getClientVendorName))
                .map(clientVendor -> mapperUtil.convert(clientVendor, new ClientVendorDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public ClientVendorDTO createClientVendor(ClientVendorDTO clientVendorDTO) {

        clientVendorDTO.setCompany(securityService.getLoggedInUser().getCompany());
        ClientVendor clientVendor = mapperUtil.convert(clientVendorDTO, new ClientVendor());
        clientVendorRepository.save(clientVendor);
        return mapperUtil.convert(clientVendor, new ClientVendorDTO());
    }

    @Override
    public List<ClientVendorDTO> listAllClientVendorsByTypeAndCompany(ClientVendorType type) {
        return clientVendorRepository.findClientVendorsByClientVendorTypeAndCompanyId(type, getCompany().id).stream()
                .map(clientVendor -> mapperUtil.convert(clientVendor, new ClientVendorDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean companyNameIsExist(ClientVendorDTO clientVendorDTO) {
        ClientVendor existingClientVendor = clientVendorRepository.findByClientVendorNameAndCompany
                (clientVendorDTO.getClientVendorName(), getCompany());
        if (existingClientVendor == null) {
            return false;
        }
        return !existingClientVendor.getId().equals(clientVendorDTO.getId());
    }

    @Override
    public List<ClientVendorType> clientVendorType() {
        return List.of(ClientVendorType.values());
    }

    @Override
    public void deleteClientVendorById(Long id) {
        ClientVendor clientVendor = clientVendorRepository.findById(id).orElseThrow(() ->
                new ClientVendorNotFoundException("Client vendor not found " + id));
        clientVendor.setIsDeleted(true);
        clientVendor.setClientVendorName(clientVendor.getClientVendorName() + "-" + clientVendor.getId());
        clientVendorRepository.save(clientVendor);
    }

    @Override
    public ClientVendorDTO updateClientVendor(Long id, ClientVendorDTO clientVendorDTO) {
        ClientVendor clientVendor = clientVendorRepository.findById(id).orElseThrow(() ->
                new ClientVendorNotFoundException("Client vendor not found " + id));
        clientVendorDTO.getAddress().setId(clientVendor.getAddress().getId());
        clientVendorDTO.setCompany(securityService.getLoggedInUser().getCompany());
        ClientVendor updatedClientVendor = mapperUtil.convert(clientVendorDTO, new ClientVendor());
        clientVendorRepository.save(updatedClientVendor);
        return mapperUtil.convert(updatedClientVendor, new ClientVendorDTO());

    }

}




