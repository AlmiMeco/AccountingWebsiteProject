
package com.cydeo.accounting_app.dto;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "date",
        "usd"
})
@Generated("jsonschema2pojo")
public class CurrencyDTO {

    @JsonProperty("date")
    private String date;
    @JsonProperty("usd")
    private Map<String, BigDecimal> usd;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("usd")
    public Map<String, BigDecimal> getUsd() {
        return usd;
    }

    @JsonProperty("usd")
    public void setUsd(Map<String, BigDecimal> usd) {
        this.usd = usd;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public BigDecimal getEuro(){
        return usd.get("eur");
    }

    public BigDecimal getBritishPound(){
        return usd.get("gbp");
    }

    public BigDecimal getCanadianDollar(){
        return usd.get("cad");
    }
    public BigDecimal getJapaneseYen(){
        return usd.get("jpy");
    }
    public BigDecimal getIndianRupee(){
        return usd.get("inr");
    }

}