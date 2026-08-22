package com.hinchmart.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAddressDto {

    private String id;
    private Long addressId;
    private String label;
    private String fullName;
    private String phone;
    private String alternatePhone;
    private String companyName;
    private String gstin;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String postalCode;

    @JsonProperty("isDefault")
    private boolean isDefault;

    private String addressType;
    private String siteAccess;
    private boolean craneAvailable;
    private boolean gatePassRequired;
    private String entryTimings;
    private LocalDateTime createdAt;

    public UserAddressDto() {
    }

    public String getId() {
        return id != null ? id : (addressId != null ? "addr_" + addressId : null);
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
        if (this.id == null && addressId != null) {
            this.id = "addr_" + addressId;
        }
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

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
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

    public boolean isCraneAvailable() {
        return craneAvailable;
    }

    public void setCraneAvailable(boolean craneAvailable) {
        this.craneAvailable = craneAvailable;
    }

    public boolean isGatePassRequired() {
        return gatePassRequired;
    }

    public void setGatePassRequired(boolean gatePassRequired) {
        this.gatePassRequired = gatePassRequired;
    }

    public String getEntryTimings() {
        return entryTimings;
    }

    public void setEntryTimings(String entryTimings) {
        this.entryTimings = entryTimings;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
