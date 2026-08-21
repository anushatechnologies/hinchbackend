package com.hinchmart.dto.request;

import jakarta.validation.constraints.NotBlank;

public class SellerVerifyOtpRequest {

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "OTP is required")
    private String otp;

    private String purpose = "REGISTER";
    private String tempToken;

    public SellerVerifyOtpRequest() {
    }

    public SellerVerifyOtpRequest(String phone, String otp, String purpose) {
        this.phone = phone;
        this.otp = otp;
        this.purpose = purpose != null ? purpose : "REGISTER";
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getTempToken() {
        return tempToken;
    }

    public void setTempToken(String tempToken) {
        this.tempToken = tempToken;
    }
}
