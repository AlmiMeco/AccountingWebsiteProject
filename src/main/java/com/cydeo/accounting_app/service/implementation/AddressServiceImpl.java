package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.entity.Address;
import com.cydeo.accounting_app.repository.AddressRepository;
import com.cydeo.accounting_app.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
    @Override
    public List<String> listOfCountries() {
        return addressRepository.findAll().stream().map(address ->
                address.getCountry()).distinct().collect(Collectors.toList());
    }

    @Override
    public String getCountryByCity(String city) {
        return addressRepository.findAddressByCity(city).orElseThrow(() ->
                new NoSuchElementException("Country not found")).getCountry();

    }
}
