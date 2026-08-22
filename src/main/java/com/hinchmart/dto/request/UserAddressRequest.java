package com.hinchmart.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UserAddressRequest {

    @NotBlank(message = "Address label is required (e.g. Main Site, Head Office)")
    private String label;

    @NotBlank(message = "Contact person name is required")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    private String phone;

    private String alternatePhone;
    private String companyName;
    private String gstin;

    @NotBlank(message = "Address Line 1 is required")
    private String addressLine1;

    private String addressLine2;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    private String country = "India";

    @NotBlank(message = "Postal code / Pincode is required")
    private String postalCode;

    private Boolean isDefault = false;
    private String addressType = "site"; // "site", "billing", "office", "warehouse"
    private String siteAccess = "heavy_trailer"; // "heavy_trailer", "truck_10w", "tempo_6w", "standard"
    private Boolean craneAvailable = false;
    private Boolean gatePassRequired = false;
    private String entryTimings = "06:00 AM - 10:00 PM";

    public UserAddressRequest() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAlternatePhone() {
        return alternatePhone;
    }

    public void setAlternatePhone(String alternatePhone) {
        this.alternatePhone = alternatePhone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getSiteAccess() {
        return siteAccess;
    }

    public void setSiteAccess(String siteAccess) {
        this.siteAccess = siteAccess;
    }

    public Boolean getCraneAvailable() {
        return craneAvailable;
    }

    public void setCraneAvailable(Boolean craneAvailable) {
        this.craneAvailable = craneAvailable;
    }

    public Boolean getGatePassRequired() {
        return gatePassRequired;
    }

    public void setGatePassRequired(Boolean gatePassRequired) {
        this.gatePassRequired = gatePassRequired;
    }

    public String getEntryTimings() {
        return entryTimings;
    }

    public void setEntryTimings(String entryTimings) {
        this.entryTimings = entryTimings;
    }
}
