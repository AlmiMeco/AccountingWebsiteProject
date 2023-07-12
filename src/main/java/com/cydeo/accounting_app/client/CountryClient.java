package com.cydeo.accounting_app.client;

import com.cydeo.accounting_app.dto.CountryResponseDTO;
import com.cydeo.accounting_app.dto.GetAuthorizationTokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(url = "https://www.universal-tutorial.com/api", name = "COUNTRY-CLIENT")
public interface CountryClient {

    @GetMapping("/getaccesstoken")
    GetAuthorizationTokenDTO getTokenForCountry(@RequestHeader("Accept") String accept,
                                                @RequestHeader("api-token") String token,
                                                @RequestHeader("user-email") String email);
    @GetMapping("/countries")
    List<CountryResponseDTO> getCountries(@RequestHeader("Authorization") String authorization);
}
