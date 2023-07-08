package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.client.CountryClient;
import com.cydeo.accounting_app.repository.AddressRepository;
import com.cydeo.accounting_app.service.AddressService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class AddressServiceImpl implements AddressService {
    @Value("${country.client.token}") String authorization;

    private final AddressRepository addressRepository;
    private final CountryClient countryClient;

    public AddressServiceImpl(AddressRepository addressRepository, CountryClient countryClient) {
        this.addressRepository = addressRepository;
        this.countryClient = countryClient;
    }
    @Override
    public List<String> listOfCountries() {
        return List.of(countryClient.getCountries(authorization).getCountryName());
    }

    @Override
    public String getCountryByCity(String city) {
        return addressRepository.findAddressByCity(city).orElseThrow(() ->
                new NoSuchElementException("Country not found")).getCountry();

    }
}
