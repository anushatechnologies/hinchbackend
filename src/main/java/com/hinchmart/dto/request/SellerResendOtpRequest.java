package com.hinchmart.dto.request;

import jakarta.validation.constraints.NotBlank;

public class SellerResendOtpRequest {

    @NotBlank(message = "Phone number is required")
    private String phone;

    private String purpose = "REGISTER";

    public SellerResendOtpRequest() {
    }

    public SellerResendOtpRequest(String phone, String purpose) {
        this.phone = phone;
        this.purpose = purpose != null ? purpose : "REGISTER";
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
