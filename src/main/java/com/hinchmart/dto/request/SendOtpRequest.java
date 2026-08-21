package com.hinchmart.dto.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.hinchmart.entity.enums.OtpPurpose;

public class SendOtpRequest {

    private String phone;
    private String identifier;
    private OtpPurpose purpose = OtpPurpose.LOGIN;

    public SendOtpRequest() {
    }

    public SendOtpRequest(String phone, OtpPurpose purpose) {
        this.phone = phone;
        this.identifier = phone;
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
