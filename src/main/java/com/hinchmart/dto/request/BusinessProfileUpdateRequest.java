package com.hinchmart.dto.request;

public class BusinessProfileUpdateRequest {

    private String companyName;
    private String gstNumber;
    private String gstin;
    private String industry;
    private String website;

    public BusinessProfileUpdateRequest() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getGstNumber() {
        return gstNumber != null ? gstNumber : gstin;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
        this.gstin = gstNumber;
    }

    public String getGstin() {
        return gstin != null ? gstin : gstNumber;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
        this.gstNumber = gstin;
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
}
