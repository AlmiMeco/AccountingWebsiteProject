package com.cydeo.accounting_app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetAuthorizationTokenDTO {

    @JsonProperty("auth_token")
    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
