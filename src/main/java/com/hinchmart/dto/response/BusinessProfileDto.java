package com.hinchmart.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BusinessProfileDto {

    private String companyName;
    private String gstNumber;
    private String industry;
    private String website;
    private String registrationNumber;
    private String taxId;
    private boolean isVerified;

    public BusinessProfileDto() {
    }

    public BusinessProfileDto(String companyName, String gstNumber, String industry, String website) {
        this.companyName = companyName;
        this.gstNumber = gstNumber;
        this.industry = industry;
        this.website = website;
        this.taxId = gstNumber;
        this.isVerified = true;
    }

    public BusinessProfileDto(String companyName, String registrationNumber, String taxId, boolean isVerified) {
        this.companyName = companyName;
        this.registrationNumber = registrationNumber;
        this.taxId = taxId;
        this.gstNumber = taxId;
        this.isVerified = isVerified;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getGstNumber() {
        return gstNumber != null ? gstNumber : taxId;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
        this.taxId = gstNumber;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getTaxId() {
        return taxId != null ? taxId : gstNumber;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
        this.gstNumber = taxId;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}

