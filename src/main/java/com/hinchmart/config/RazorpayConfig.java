package com.hinchmart.config;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayConfig {

    private static final Logger log = LoggerFactory.getLogger(RazorpayConfig.class);

    @Value("${razorpay.key-id:rzp_live_TO6q7NUVnPM6bA}")
    private String keyId;

    @Value("${razorpay.key-secret:pRMq2obuE51XoJlH3NDyUl9w}")
    private String keySecret;

    @Value("${razorpay.webhook-secret:}")
    private String webhookSecret;

    @Value("${razorpay.currency:INR}")
    private String currency;

    @Value("${razorpay.company-name:HinchMart}")
    private String companyName;

    @Bean
    public RazorpayClient razorpayClient() {
        try {
            log.info("Initializing Razorpay Client with Key ID: {}", keyId != null && keyId.length() > 6 ? keyId.substring(0, 8) + "..." : "EMPTY");
            return new RazorpayClient(keyId, keySecret);
        } catch (RazorpayException e) {
            log.error("Failed to initialize RazorpayClient: {}", e.getMessage(), e);
            throw new RuntimeException("Could not initialize Razorpay Client: " + e.getMessage(), e);
        }
    }

    public String getKeyId() {
        return keyId;
    }

    public String getKeySecret() {
        return keySecret;
    }

    public String getWebhookSecret() {
        return webhookSecret;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCompanyName() {
        return companyName;
    }
}
