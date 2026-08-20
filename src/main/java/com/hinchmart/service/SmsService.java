package com.hinchmart.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

@Service
public class SmsService {

    private static final Logger log = LoggerFactory.getLogger(SmsService.class);

    private final HttpClient httpClient;

    @Value("${hinchmart.sms.provider:FAST2SMS}")
    private String smsProvider; // TWILIO, FAST2SMS, MSG91, MOCK

    @Value("${hinchmart.sms.enabled:true}")
    private boolean smsEnabled;

    // Twilio Configuration
    @Value("${hinchmart.sms.twilio.account-sid:}")
    private String twilioAccountSid;

    @Value("${hinchmart.sms.twilio.auth-token:}")
    private String twilioAuthToken;

    @Value("${hinchmart.sms.twilio.from-phone:}")
    private String twilioFromPhone;

    // Fast2SMS Configuration (Popular Indian SMS gateway)
    @Value("${hinchmart.sms.fast2sms.api-key:}")
    private String fast2smsApiKey;

    // MSG91 Configuration
    @Value("${hinchmart.sms.msg91.auth-key:}")
    private String msg91AuthKey;

    @Value("${hinchmart.sms.msg91.template-id:}")
    private String msg91TemplateId;

    public SmsService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Dispatches real SMS to the given phone number via configured SMS Gateway.
     */
    @Async
    public void sendOtpSms(String phoneNumber, String otpCode, String purpose, int expiryMinutes) {
        if (!smsEnabled) {
            log.info("SMS service is disabled in configuration. Skipping SMS to {}", phoneNumber);
            return;
        }

        String formattedPhone = formatPhoneNumber(phoneNumber);
        String messageText = "Your HinchMart verification code is " + otpCode + ". Valid for " + expiryMinutes + " minutes. Do not share with anyone.";

        log.info("Dispatching SMS via provider [{}] to {}...", smsProvider, formattedPhone);

        try {
            switch (smsProvider.toUpperCase()) {
                case "TWILIO":
                    sendViaTwilio(formattedPhone, messageText);
                    break;
                case "FAST2SMS":
                    sendViaFast2Sms(formattedPhone, otpCode);
                    break;
                case "MSG91":
                    sendViaMsg91(formattedPhone, otpCode);
                    break;
                case "MOCK":
                default:
                    log.info(">>> [MOCK SMS GATEWAY] To: {} | Text: '{}'", formattedPhone, messageText);
                    break;
            }
        } catch (Exception e) {
            log.error("Failed to dispatch SMS to {}: {}", formattedPhone, e.getMessage(), e);
        }
    }

    private void sendViaTwilio(String toPhone, String messageText) throws Exception {
        if (twilioAccountSid == null || twilioAccountSid.trim().isEmpty() || twilioAuthToken == null || twilioAuthToken.trim().isEmpty()) {
            log.warn("Twilio credentials not configured in application.properties. SMS dispatch skipped for {}", toPhone);
            return;
        }

        String url = "https://api.twilio.com/2010-04-01/Accounts/" + twilioAccountSid + "/Messages.json";
        String formBody = "To=" + URLEncoder.encode(toPhone, StandardCharsets.UTF_8)
                + "&From=" + URLEncoder.encode(twilioFromPhone, StandardCharsets.UTF_8)
                + "&Body=" + URLEncoder.encode(messageText, StandardCharsets.UTF_8);

        String auth = twilioAccountSid + ":" + twilioAuthToken;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Basic " + encodedAuth)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .timeout(Duration.ofSeconds(15))
                .POST(HttpRequest.BodyPublishers.ofString(formBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            log.info("Successfully delivered SMS via Twilio to {}", toPhone);
        } else {
            log.error("Twilio SMS failed with status {}: {}", response.statusCode(), response.body());
        }
    }

    private void sendViaFast2Sms(String toPhone, String otpCode) throws Exception {
        if (fast2smsApiKey == null || fast2smsApiKey.trim().isEmpty()) {
            log.warn("Fast2SMS API Key not configured in application.properties. SMS dispatch skipped for {}", toPhone);
            return;
        }

        // Strip +91 for Fast2SMS Indian 10-digit format
        String cleanNumber = toPhone.replace("+91", "").replaceAll("[^0-9]", "");

        // Fast2SMS Quick OTP route
        String url = "https://www.fast2sms.com/dev/bulkV2?authorization=" + URLEncoder.encode(fast2smsApiKey, StandardCharsets.UTF_8)
                + "&route=otp&variables_values=" + URLEncoder.encode(otpCode, StandardCharsets.UTF_8)
                + "&flash=0&numbers=" + URLEncoder.encode(cleanNumber, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            log.info("Successfully delivered SMS via Fast2SMS to {}", cleanNumber);
        } else {
            log.error("Fast2SMS API failed with status {}: {}", response.statusCode(), response.body());
        }
    }

    private void sendViaMsg91(String toPhone, String otpCode) throws Exception {
        if (msg91AuthKey == null || msg91AuthKey.trim().isEmpty()) {
            log.warn("MSG91 Auth Key not configured in application.properties. SMS dispatch skipped for {}", toPhone);
            return;
        }

        String cleanNumber = toPhone.replace("+", "");
        String url = "https://control.msg91.com/api/v5/otp?template_id=" + URLEncoder.encode(msg91TemplateId, StandardCharsets.UTF_8)
                + "&mobile=" + URLEncoder.encode(cleanNumber, StandardCharsets.UTF_8)
                + "&authkey=" + URLEncoder.encode(msg91AuthKey, StandardCharsets.UTF_8)
                + "&otp=" + URLEncoder.encode(otpCode, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(15))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            log.info("Successfully delivered SMS via MSG91 to {}", toPhone);
        } else {
            log.error("MSG91 API failed with status {}: {}", response.statusCode(), response.body());
        }
    }

    private String formatPhoneNumber(String phone) {
        if (phone == null) return "";
        String cleaned = phone.trim().replaceAll("[^0-9+]", "");
        if (cleaned.length() == 10) {
            return "+91" + cleaned; // Standard Indian E.164 format
        }
        if (!cleaned.startsWith("+") && cleaned.startsWith("91") && cleaned.length() == 12) {
            return "+" + cleaned;
        }
        return cleaned;
    }
}
