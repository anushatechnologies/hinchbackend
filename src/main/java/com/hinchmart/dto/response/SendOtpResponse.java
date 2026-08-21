package com.hinchmart.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendOtpResponse {

    private boolean success = true;
    private String message;
    private int expiresIn = 300; // 5 minutes in seconds
    private int resendAfter = 60; // 60 seconds

    public SendOtpResponse() {
    }

    public SendOtpResponse(boolean success, String message, int expiresIn, int resendAfter) {
        this.success = success;
        this.message = message;
        this.expiresIn = expiresIn;
        this.resendAfter = resendAfter;
    }

    public static SendOtpResponse success(String message, int expiresIn, int resendAfter) {
        return new SendOtpResponse(true, message, expiresIn, resendAfter);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public int getResendAfter() {
        return resendAfter;
    }

    public void setResendAfter(int resendAfter) {
        this.resendAfter = resendAfter;
    }
}
