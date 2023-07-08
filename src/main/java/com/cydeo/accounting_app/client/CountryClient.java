package com.cydeo.accounting_app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(url = "https://www.universal-tutorial.com/api", name = "COUNTRY-CLIENT")
public interface CountryClient {
    @GetMapping("/countries")
    List<String> getCountries();

}
// uNAfy7_UQW5f1FvtdEzxepNiWs4Yiz-WKA3n_2qHXHj1T_ITEbZ8AOVaUd-dxloJDL4



