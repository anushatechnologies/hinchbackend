package com.hinchmart.service;

import com.hinchmart.dto.request.SendOtpRequest;
import com.hinchmart.dto.response.SendOtpResponse;
import com.hinchmart.entity.OtpVerification;
import com.hinchmart.entity.User;
import com.hinchmart.entity.VerificationToken;
import com.hinchmart.entity.enums.OtpPurpose;
import com.hinchmart.exception.BadRequestException;
import com.hinchmart.repository.OtpVerificationRepository;
import com.hinchmart.repository.UserRepository;
import com.hinchmart.repository.VerificationTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class OtpService {

    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);

    private final OtpVerificationRepository otpVerificationRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final SmsService smsService;

    @Value("${hinchmart.otp.expiration-minutes:5}")
    private int expirationMinutes;

    @Value("${hinchmart.otp.mock-mode:false}")
    private boolean mockMode;

    @Value("${hinchmart.otp.default-test-code:123456}")
    private String defaultTestCode;

    public OtpService(OtpVerificationRepository otpVerificationRepository,
                      VerificationTokenRepository verificationTokenRepository,
                      UserRepository userRepository,
                      EmailService emailService,
                      SmsService smsService) {
        this.otpVerificationRepository = otpVerificationRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.smsService = smsService;
    }

    /**
     * Formats phone number for user-facing messaging (e.g. "+91 98765 43210")
     */
    public String formatPhoneForDisplay(String phone) {
        if (phone == null) return "";
        String clean = phone.trim();
        if (clean.startsWith("+91") && clean.length() == 13) {
            return "+91 " + clean.substring(3, 8) + " " + clean.substring(8);
        } else if (clean.length() == 10) {
            return "+91 " + clean.substring(0, 5) + " " + clean.substring(5);
        }
        return clean;
    }

    /**
     * Generates a cryptographically secure 6-digit OTP and dispatches it via Real Email and/or Real SMS.
     * Returns structured SendOtpResponse with countdown timers.
     */
    @Transactional
    public SendOtpResponse sendOtp(SendOtpRequest request) {
        String identifier = request.getPhone() != null ? request.getPhone() : request.getIdentifier();
        if (identifier == null || identifier.trim().isEmpty()) {
            throw new BadRequestException("Phone number or email is required");
        }

        String otpCode = generateAndSendOtp(identifier, request.getPurpose());
        String displayIdentifier = formatPhoneForDisplay(identifier);
        int expiresInSeconds = expirationMinutes * 60;
        int resendAfterSeconds = 60;

        return new SendOtpResponse(
                true,
                "OTP sent successfully to " + displayIdentifier,
                expiresInSeconds,
                resendAfterSeconds
        );
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
        if (identifier == null || identifier.trim().isEmpty()) {
            throw new BadRequestException("Phone number or email is required");
        }
        if (otpCode == null || otpCode.trim().isEmpty()) {
            throw new BadRequestException("OTP code is required");
        }

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
            throw new BadRequestException("The OTP entered is incorrect or has expired. Please request a new code.");
        }

        if (otp.isExpired()) {
            throw new BadRequestException("The OTP entered is incorrect or has expired. Please request a new code.");
        }

        if (purpose != null && otp.getPurpose() != purpose && otp.getPurpose() != OtpPurpose.VERIFICATION) {
            throw new BadRequestException("OTP was not issued for the requested action.");
        }

        otp.setUsed(true);
        otpVerificationRepository.save(otp);
        return true;
    }

    @Transactional
    public String createVerificationToken(String phone) {
        String tokenStr = "ver_tok_" + UUID.randomUUID().toString().replace("-", "");
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(30); // 30 minutes to complete registration
        VerificationToken token = new VerificationToken(tokenStr, phone.trim(), expiry);
        verificationTokenRepository.save(token);
        return tokenStr;
    }

    @Transactional(readOnly = true)
    public VerificationToken validateVerificationToken(String tokenStr) {
        if (tokenStr == null || tokenStr.trim().isEmpty()) {
            throw new BadRequestException("Verification token is required for registration");
        }
        VerificationToken token = verificationTokenRepository.findByTokenAndIsUsedFalse(tokenStr.trim())
                .orElseThrow(() -> new BadRequestException("Invalid or already used verification token. Please verify your phone number again."));

        if (token.isExpired()) {
            throw new BadRequestException("Verification token has expired. Please verify your phone number again.");
        }
        return token;
    }

    @Transactional
    public void consumeVerificationToken(VerificationToken token) {
        token.setUsed(true);
        verificationTokenRepository.save(token);
    }
}
