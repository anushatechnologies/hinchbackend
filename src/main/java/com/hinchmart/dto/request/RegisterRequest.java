package com.hinchmart.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.hinchmart.entity.enums.Role;

public class RegisterRequest {

    private String verificationToken;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private String accountType; // "individual", "business", "contractor", "vendor"
    private Role role;

    // Profile fields
    private String companyName;
    private String gstin;
    private String pan;
    private String panNumber;
    private String businessType;
    private String address;
    private String city;
    private String state;
    private String pincode;

    public RegisterRequest() {
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        if (fullName != null && !fullName.trim().isEmpty()) {
            return fullName.trim();
        }
        if (firstName != null && !firstName.trim().isEmpty()) {
            return (firstName.trim() + (lastName != null && !lastName.trim().isEmpty() ? " " + lastName.trim() : "")).trim();
        }
        return "HinchMart User";
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
        if (this.role == null) {
            if ("vendor".equalsIgnoreCase(accountType) || "seller".equalsIgnoreCase(accountType)) {
                this.role = Role.SELLER;
            } else {
                this.role = Role.BUYER;
            }
        }
    }

    public Role getRole() {
        if (role != null) {
            return role;
        }
        if (accountType != null && ("vendor".equalsIgnoreCase(accountType) || "seller".equalsIgnoreCase(accountType))) {
            return Role.SELLER;
        }
        return Role.BUYER;
    }

    public void setRole(Role role) {
        this.role = role;
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
        return pan != null && !pan.trim().isEmpty() ? pan.trim() : (panNumber != null ? panNumber.trim() : null);
    }

    public void setPan(String pan) {
        this.pan = pan;
        if (this.panNumber == null) {
            this.panNumber = pan;
        }
    }

    public String getPanNumber() {
        return panNumber != null && !panNumber.trim().isEmpty() ? panNumber.trim() : (pan != null ? pan.trim() : null);
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
        if (this.pan == null) {
            this.pan = panNumber;
        }
    }

    public String getBusinessType() {
        if (businessType != null && !businessType.trim().isEmpty()) {
            return businessType;
        }
        if (accountType != null && !accountType.trim().isEmpty()) {
            return accountType;
        }
        return "Business";
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
