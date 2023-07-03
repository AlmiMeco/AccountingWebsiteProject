package com.cydeo.accounting_app.client;

import com.cydeo.accounting_app.dto.CurrencyDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url="https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/",name="CURRENCY-CLIENT")
public interface CurrencyClient {

    @GetMapping("/usd.json")
    CurrencyDTO getCurrency();

}
