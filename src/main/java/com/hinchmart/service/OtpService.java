package com.hinchmart.service;

import com.hinchmart.entity.OtpVerification;
import com.hinchmart.entity.User;
import com.hinchmart.entity.enums.OtpPurpose;
import com.hinchmart.exception.BadRequestException;
import com.hinchmart.repository.OtpVerificationRepository;
import com.hinchmart.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OtpService {

    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);

    private final OtpVerificationRepository otpVerificationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final SmsService smsService;

    @Value("${hinchmart.otp.expiration-minutes:10}")
    private int expirationMinutes;

    @Value("${hinchmart.otp.mock-mode:false}")
    private boolean mockMode;

    @Value("${hinchmart.otp.default-test-code:123456}")
    private String defaultTestCode;

    public OtpService(OtpVerificationRepository otpVerificationRepository,
                      UserRepository userRepository,
                      EmailService emailService,
                      SmsService smsService) {
        this.otpVerificationRepository = otpVerificationRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.smsService = smsService;
    }

    /**
     * Generates a cryptographically secure 6-digit OTP and dispatches it via Real Email and/or Real SMS.
     */
    @Transactional
    public String generateAndSendOtp(String identifier, OtpPurpose purpose) {
        String cleanIdentifier = identifier.trim();
        OtpPurpose cleanPurpose = purpose != null ? purpose : OtpPurpose.LOGIN;

        // 1. Generate 6-digit numeric OTP
        String otpCode;
        if (mockMode) {
            otpCode = defaultTestCode;
        } else {
            SecureRandom random = new SecureRandom();
            int num = 100000 + random.nextInt(900000);
            otpCode = String.valueOf(num);
        }

        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(expirationMinutes);

        // 2. Persist OTP in database
        OtpVerification otpVerification = new OtpVerification(cleanIdentifier, otpCode, cleanPurpose, expiryTime);
        otpVerificationRepository.save(otpVerification);

        logger.info(">>> [OTP ISSUED] Code: {} for identifier: {} (Purpose: {}, Expires in: {} mins)",
                otpCode, cleanIdentifier, cleanPurpose, expirationMinutes);

        // 3. Dispatch to Real Channels (Email / SMS)
        dispatchOtpToChannels(cleanIdentifier, otpCode, cleanPurpose.name());

        return otpCode;
    }

    private void dispatchOtpToChannels(String identifier, String otpCode, String purposeName) {
        boolean isEmail = identifier.contains("@");
        boolean isPhone = identifier.matches("^[+]?[0-9]{10,14}$");

        // Direct channel dispatch
        if (isEmail) {
            emailService.sendOtpEmail(identifier, otpCode, purposeName, expirationMinutes);
        } else if (isPhone) {
            smsService.sendOtpSms(identifier, otpCode, purposeName, expirationMinutes);
        }

        // Check if identifier matches a registered user to also dispatch to their secondary channel
        Optional<User> userOpt = isEmail ? userRepository.findByEmail(identifier) : userRepository.findByPhone(identifier);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (isEmail && user.getPhone() != null && !user.getPhone().trim().isEmpty()) {
                smsService.sendOtpSms(user.getPhone(), otpCode, purposeName, expirationMinutes);
            } else if (isPhone && user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
                emailService.sendOtpEmail(user.getEmail(), otpCode, purposeName, expirationMinutes);
            }
        }
    }

    @Transactional
    public boolean verifyOtp(String identifier, String otpCode, OtpPurpose purpose) {
        String cleanIdentifier = identifier.trim();
        String cleanOtp = otpCode.trim();

        // Check for matching active OTP in database
        OtpVerification otp = otpVerificationRepository
                .findTopByIdentifierAndOtpCodeAndIsUsedFalseOrderByCreatedAtDesc(cleanIdentifier, cleanOtp)
                .orElse(null);

        // If mock mode is enabled, also allow default test code
        if (otp == null && mockMode && defaultTestCode.equals(cleanOtp)) {
            logger.info("Mock default OTP accepted for identifier: {}", cleanIdentifier);
            return true;
        }

        if (otp == null) {
            throw new BadRequestException("Invalid or expired OTP code.");
        }

        if (otp.isExpired()) {
            throw new BadRequestException("OTP code has expired. Please request a new one.");
        }

        if (purpose != null && otp.getPurpose() != purpose) {
            throw new BadRequestException("OTP was not issued for the requested action.");
        }

        otp.setUsed(true);
        otpVerificationRepository.save(otp);
        return true;
    }
}
