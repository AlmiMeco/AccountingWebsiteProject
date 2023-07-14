package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.client.CountryClient;
import com.cydeo.accounting_app.dto.CountryResponseDTO;
import com.cydeo.accounting_app.dto.GetAuthorizationTokenDTO;
import com.cydeo.accounting_app.repository.AddressRepository;
import com.cydeo.accounting_app.service.AddressService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {
    @Value("${get.token.For.Country.Accept}")
    private String accept;

    @Value("${get.token.For.Country.api-token}")
    private String apiToken;

    @Value("${get.token.For.Country.user-email}")
    private String userEmail;

    private final AddressRepository addressRepository;
    private final CountryClient countryClient;

    public AddressServiceImpl(AddressRepository addressRepository, CountryClient countryClient) {
        this.addressRepository = addressRepository;
        this.countryClient = countryClient;
    }

    private String getAuthorizationToken() {
        GetAuthorizationTokenDTO tokenForCountry = countryClient.getTokenForCountry(accept, apiToken, userEmail);
        if (tokenForCountry != null) {
            String token = "Bearer " + tokenForCountry.getAuthToken();
            return token;
        }
        throw new NoSuchElementException("Authorization token not found or reload the page again, please ");
    }

    @Override
    public List<String> listOfCountries() {
        List<CountryResponseDTO> countries = countryClient.getCountries(getAuthorizationToken());
        return countries.stream()
                .map(CountryResponseDTO::getCountryName)
                .collect(Collectors.toList());
    }

    @Override
    public String getCountryByCity(String city) {
        return addressRepository.findAddressByCity(city)
                .orElseThrow(() -> new NoSuchElementException("Country not found"))
                .getCountry();
    }
}
