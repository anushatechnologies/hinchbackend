package com.hinchmart.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.hinchmart.entity.enums.OtpPurpose;

public class VerifyOtpRequest {

    private String phone;
    private String identifier;
    private String otp;
    private String otpCode;
    private OtpPurpose purpose = OtpPurpose.LOGIN;

    public VerifyOtpRequest() {
    }

    public VerifyOtpRequest(String phone, String otp, OtpPurpose purpose) {
        this.phone = phone;
        this.identifier = phone;
        this.otp = otp;
        this.otpCode = otp;
        this.purpose = purpose != null ? purpose : OtpPurpose.LOGIN;
    }

    public String getPhone() {
        return phone != null && !phone.trim().isEmpty() ? phone.trim() : (identifier != null ? identifier.trim() : null);
    }

    public void setPhone(String phone) {
        this.phone = phone;
        if (this.identifier == null) {
            this.identifier = phone;
        }
    }

    public String getIdentifier() {
        return identifier != null && !identifier.trim().isEmpty() ? identifier.trim() : (phone != null ? phone.trim() : null);
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
        if (this.phone == null) {
            this.phone = identifier;
        }
    }

    public String getOtp() {
        return otp != null && !otp.trim().isEmpty() ? otp.trim() : (otpCode != null ? otpCode.trim() : null);
    }

    public void setOtp(String otp) {
        this.otp = otp;
        if (this.otpCode == null) {
            this.otpCode = otp;
        }
    }

    public String getOtpCode() {
        return otpCode != null && !otpCode.trim().isEmpty() ? otpCode.trim() : (otp != null ? otp.trim() : null);
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
        if (this.otp == null) {
            this.otp = otpCode;
        }
    }

    public OtpPurpose getPurpose() {
        return purpose != null ? purpose : OtpPurpose.LOGIN;
    }

    @JsonSetter("purpose")
    public void setPurpose(Object purposeObj) {
        if (purposeObj instanceof OtpPurpose) {
            this.purpose = (OtpPurpose) purposeObj;
        } else if (purposeObj != null) {
            String str = purposeObj.toString().trim().toUpperCase();
            try {
                this.purpose = OtpPurpose.valueOf(str);
            } catch (Exception e) {
                if ("REGISTER".equalsIgnoreCase(str)) {
                    this.purpose = OtpPurpose.REGISTRATION;
                } else {
                    this.purpose = OtpPurpose.LOGIN;
                }
            }
        }
    }
}
