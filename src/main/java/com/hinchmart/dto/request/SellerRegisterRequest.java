package com.hinchmart.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SellerRegisterRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String mobileNumber;
    private String phone;

    @NotBlank(message = "Company name is required")
    private String companyName;

    private String businessType = "Distributor";

    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$", message = "Invalid GSTIN format (15 characters alphanumeric)")
    private String gstin;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String confirmPassword;
    private Boolean acceptTerms = true;

    public SellerRegisterRequest() {
    }

    public String getFullName() {
        return fullName;
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

    public String getMobileNumber() {
        return mobileNumber != null && !mobileNumber.trim().isEmpty() ? mobileNumber.trim() : (phone != null ? phone.trim() : null);
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        if (this.phone == null) {
            this.phone = mobileNumber;
        }
    }

    public String getPhone() {
        return phone != null && !phone.trim().isEmpty() ? phone.trim() : (mobileNumber != null ? mobileNumber.trim() : null);
    }

    public void setPhone(String phone) {
        this.phone = phone;
        if (this.mobileNumber == null) {
            this.mobileNumber = phone;
        }
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Boolean getAcceptTerms() {
        return acceptTerms;
    }

    public void setAcceptTerms(Boolean acceptTerms) {
        this.acceptTerms = acceptTerms;
    }
}
