package com.cydeo.accounting_app.service;

import com.cydeo.accounting_app.entity.ChargeRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    private final String secretKey = "sk_test_51NSOnCGGEydpGaBd2jggppmLbqmDRRdegUv3T9W7XlQiohhTOFTOQI02Zn3zu7rjLM90KBF3wo7ZA04TU0sygd6900K6BXGxTV";

    @PostConstruct
    public void init(){
        Stripe.apiKey = secretKey;
    }

    public Charge charge(ChargeRequest chargeRequest) throws StripeException {

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequest.getAmount());
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("description", chargeRequest.getDescription());
        chargeParams.put("source", chargeRequest.getStripeToken());

        return Charge.create(chargeParams);

    }

}
