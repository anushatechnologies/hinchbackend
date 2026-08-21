package com.hinchmart.dto.request;

public class SellerProfileUpdateRequest {

    private String companyName;
    private String gstin;
    private String pan;
    private String panNumber;
    private String businessType;
    private String warehouseAddress;
    private String city;
    private String state;
    private String district;
    private String area;
    private String pincode;
    private String completeAddress;
    private String country = "India";

    private Integer establishedYear;
    private String employees;
    private String employeeCount;
    private String website;
    private String companyEmail;
    private String businessPhone;
    private String description;

    public SellerProfileUpdateRequest() {
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

    public String getPan() {
        return pan != null ? pan : panNumber;
    }

    public void setPan(String pan) {
        this.pan = pan;
        if (this.panNumber == null) {
            this.panNumber = pan;
        }
    }

    public String getPanNumber() {
        return panNumber != null ? panNumber : pan;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
        if (this.pan == null) {
            this.pan = panNumber;
        }
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getWarehouseAddress() {
        return warehouseAddress != null ? warehouseAddress : completeAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
        if (this.completeAddress == null) {
            this.completeAddress = warehouseAddress;
        }
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCompleteAddress() {
        return completeAddress != null ? completeAddress : warehouseAddress;
    }

    public void setCompleteAddress(String completeAddress) {
        this.completeAddress = completeAddress;
        if (this.warehouseAddress == null) {
            this.warehouseAddress = completeAddress;
        }
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getEstablishedYear() {
        return establishedYear;
    }

    public void setEstablishedYear(Integer establishedYear) {
        this.establishedYear = establishedYear;
    }

    public String getEmployees() {
        return employees != null && !employees.trim().isEmpty() ? employees : employeeCount;
    }

    public void setEmployees(String employees) {
        this.employees = employees;
        if (this.employeeCount == null) {
            this.employeeCount = employees;
        }
    }

    public String getEmployeeCount() {
        return employeeCount != null && !employeeCount.trim().isEmpty() ? employeeCount : employees;
    }

    public void setEmployeeCount(String employeeCount) {
        this.employeeCount = employeeCount;
        if (this.employees == null) {
            this.employees = employeeCount;
        }
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
