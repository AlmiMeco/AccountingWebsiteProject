package com.cydeo.accounting_app.service;

import java.util.List;

public interface AddressService {

    List<String> listOfCountries();

    String getCountryByCity (String city);
}
