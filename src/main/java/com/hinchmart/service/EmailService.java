package com.hinchmart.service;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@hinchmart.com}")
    private String fromEmail;

    @Value("${hinchmart.mail.from-name:HinchMart Verification}")
    private String fromName;

    @Value("${hinchmart.mail.enabled:true}")
    private boolean mailEnabled;

    public EmailService(@Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Dispatches real HTML OTP email to the user's email address.
     */
    @Async
    public void sendOtpEmail(String toEmail, String otpCode, String purpose, int expiryMinutes) {
        if (!mailEnabled) {
            log.info("Email service is disabled in configuration. Skipping email dispatch to {}", toEmail);
            return;
        }

        if (mailSender == null) {
            log.warn("JavaMailSender is not configured. SMTP settings may be missing in application.properties. OTP: {} for {}", otpCode, toEmail);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            helper.setFrom(fromEmail, fromName);
            helper.setTo(toEmail);
            helper.setSubject("Your HinchMart Verification Code: " + otpCode);

            String htmlContent = buildOtpHtmlTemplate(otpCode, purpose, expiryMinutes);
            helper.setText(htmlContent, true);

            log.info("Sending OTP verification email to {} (Purpose: {})...", toEmail, purpose);
            mailSender.send(message);
            log.info("Successfully delivered OTP email to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send OTP email to {}: {}", toEmail, e.getMessage(), e);
        }
    }

    private String buildOtpHtmlTemplate(String otpCode, String purpose, int expiryMinutes) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "  body { font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; background-color: #0B0F19; margin: 0; padding: 20px; color: #F9FAFB; }"
                + "  .container { max-width: 520px; margin: 0 auto; background-color: #111827; border-radius: 12px; border: 1px solid #1F2937; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.5); }"
                + "  .header { background: linear-gradient(135deg, #3B82F6 0%, #8B5CF6 100%); padding: 30px 20px; text-align: center; }"
                + "  .header h1 { margin: 0; color: #FFFFFF; font-size: 24px; font-weight: 700; letter-spacing: -0.5px; }"
                + "  .content { padding: 30px 25px; text-align: center; line-height: 1.6; }"
                + "  .desc { font-size: 15px; color: #9CA3AF; margin-bottom: 25px; }"
                + "  .otp-box { background: #1E293B; border: 2px dashed #3B82F6; border-radius: 10px; padding: 18px; display: inline-block; margin: 10px auto 25px; letter-spacing: 8px; font-size: 32px; font-weight: 800; color: #38BDF8; font-family: monospace; }"
                + "  .expiry { font-size: 13px; color: #F59E0B; font-weight: 600; margin-bottom: 20px; }"
                + "  .footer { border-top: 1px solid #1F2937; padding: 20px; text-align: center; font-size: 12px; color: #6B7280; background-color: #0d1322; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "  <div class='header'>"
                + "    <h1>HINCHMART</h1>"
                + "    <div style='font-size: 12px; color: #E0E7FF; margin-top: 4px;'>B2B Industrial Marketplace</div>"
                + "  </div>"
                + "  <div class='content'>"
                + "    <h2 style='font-size: 18px; margin-top: 0; color: #F3F4F6;'>Security Verification Code</h2>"
                + "    <p class='desc'>Use the following One-Time Password (OTP) to complete your <strong>" + purpose.replace("_", " ") + "</strong> request.</p>"
                + "    <div class='otp-box'>" + otpCode + "</div>"
                + "    <div class='expiry'>⏳ Valid for the next " + expiryMinutes + " minutes. Do not share this code with anyone.</div>"
                + "    <p style='font-size: 13px; color: #94A3B8;'>If you did not request this verification, please secure your account immediately.</p>"
                + "  </div>"
                + "  <div class='footer'>"
                + "    &copy; " + java.time.Year.now().getValue() + " HinchMart Marketplace Pvt Ltd. All rights reserved.<br>Automated security notification. Please do not reply."
                + "  </div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }
}
