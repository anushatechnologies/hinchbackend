package com.hinchmart;

import com.hinchmart.config.RazorpayConfig;
import com.hinchmart.dto.response.RazorpayConfigDto;
import com.hinchmart.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RazorpayConfig razorpayConfig;

    @Test
    public void testRazorpayConfigLoaded() {
        RazorpayConfigDto config = paymentService.getPublicConfig();
        assertNotNull(config);
        assertEquals("rzp_live_TO6q7NUVnPM6bA", config.getKeyId());
        assertEquals("INR", config.getCurrency());
        assertEquals("HinchMart", config.getCompanyName());
    }

    @Test
    public void testHmacSha256SignatureVerificationSuccess() throws Exception {
        String orderId = "order_test_123456";
        String paymentId = "pay_test_789012";
        String secret = razorpayConfig.getKeySecret();

        // Generate valid signature using known secret
        String data = orderId + "|" + paymentId;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        String validSignature = hexString.toString();

        boolean result = paymentService.verifyRazorpaySignature(orderId, paymentId, validSignature);
        assertTrue(result, "Signature verification should succeed for authentic HMAC-SHA256 signature");
    }

    @Test
    public void testHmacSha256SignatureVerificationFailsOnTampering() {
        String orderId = "order_test_123456";
        String paymentId = "pay_test_789012";
        String fakeSignature = "invalid_tampered_signature_123456789";

        boolean result = paymentService.verifyRazorpaySignature(orderId, paymentId, fakeSignature);
        assertFalse(result, "Signature verification should fail for tampered/fake signature");
    }
}
