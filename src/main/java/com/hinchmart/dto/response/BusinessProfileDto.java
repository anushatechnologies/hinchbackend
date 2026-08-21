package com.hinchmart.dto.response;

public class BusinessProfileDto {

    private String companyName;
    private String registrationNumber;
    private String taxId;
    private boolean isVerified;

    public BusinessProfileDto() {
    }

    public BusinessProfileDto(String companyName, String registrationNumber, String taxId, boolean isVerified) {
        this.companyName = companyName;
        this.registrationNumber = registrationNumber;
        this.taxId = taxId;
        this.isVerified = isVerified;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
