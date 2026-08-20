package com.hinchmart.dto.response;

public class RazorpayConfigDto {

    private String keyId;
    private String currency;
    private String companyName;

    public RazorpayConfigDto() {
    }

    public RazorpayConfigDto(String keyId, String currency, String companyName) {
        this.keyId = keyId;
        this.currency = currency;
        this.companyName = companyName;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
