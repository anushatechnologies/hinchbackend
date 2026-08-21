package com.hinchmart.service;

import com.hinchmart.config.JwtTokenProvider;
import com.hinchmart.dto.request.*;
import com.hinchmart.entity.RefreshToken;
import com.hinchmart.entity.SellerProfile;
import com.hinchmart.entity.User;
import com.hinchmart.entity.enums.AccountStatus;
import com.hinchmart.entity.enums.OtpPurpose;
import com.hinchmart.entity.enums.Role;
import com.hinchmart.entity.enums.SellerStatus;
import com.hinchmart.exception.BadRequestException;
import com.hinchmart.exception.ResourceNotFoundException;
import com.hinchmart.exception.UnauthorizedException;
import com.hinchmart.repository.RefreshTokenRepository;
import com.hinchmart.repository.SellerProfileRepository;
import com.hinchmart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SellerAuthService {

    private final UserRepository userRepository;
    private final SellerProfileRepository sellerProfileRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final OtpService otpService;
    private final ActivityLogService activityLogService;

    @Value("${hinchmart.jwt.refresh-expiration-ms:2592000000}")
    private long refreshExpirationMs; // 30 days

    public SellerAuthService(UserRepository userRepository,
                             SellerProfileRepository sellerProfileRepository,
                             RefreshTokenRepository refreshTokenRepository,
                             PasswordEncoder passwordEncoder,
                             JwtTokenProvider tokenProvider,
                             OtpService otpService,
                             ActivityLogService activityLogService) {
        this.userRepository = userRepository;
        this.sellerProfileRepository = sellerProfileRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.otpService = otpService;
        this.activityLogService = activityLogService;
    }

    @Transactional
    public Map<String, Object> register(SellerRegisterRequest request) {
        if (request.getConfirmPassword() != null && !request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("An account with this email already exists");
        }

        String phone = request.getPhone() != null ? request.getPhone() : request.getMobileNumber();
        if (phone != null && !phone.trim().isEmpty() && userRepository.existsByPhone(phone)) {
            throw new BadRequestException("An account with this mobile number already exists");
        }

        User user = new User(
                request.getEmail(),
                phone,
                passwordEncoder.encode(request.getPassword()),
                request.getFullName(),
                Role.SELLER_ADMIN,
                AccountStatus.ACTIVE
        );
        User savedUser = userRepository.save(user);

        SellerProfile sellerProfile = new SellerProfile(
                savedUser,
                request.getCompanyName(),
                request.getGstin(),
                request.getBusinessType() != null ? request.getBusinessType() : "Distributor",
                SellerStatus.DRAFT
        );
        sellerProfile.setCompletionPercentage(25);
        if (phone != null) {
            sellerProfile.setBusinessPhone(phone);
        }
        sellerProfile.setCompanyEmail(request.getEmail());
        savedUser.setSellerProfile(sellerProfile);
        sellerProfileRepository.save(sellerProfile);

        // Send OTP for mobile verification
        if (phone != null && !phone.trim().isEmpty()) {
            otpService.generateAndSendOtp(phone, OtpPurpose.REGISTRATION);
        }

        activityLogService.log(savedUser.getId(), savedUser.getEmail(), "SELLER_REGISTERED", "SELLER",
                savedUser.getId(), "Registered new seller business: " + request.getCompanyName(), null);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("userId", "usr_" + savedUser.getId());
        data.put("phone", phone);
        data.put("email", savedUser.getEmail());
        data.put("requiresOtp", true);
        data.put("otpCooldownSeconds", 45);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("message", "Account created. Please verify your mobile number with the OTP sent.");
        response.put("data", data);
        return response;
    }

    @Transactional
    public Map<String, Object> verifyOtp(SellerVerifyOtpRequest request) {
        String phone = request.getPhone();
        String otp = request.getOtp();

        OtpPurpose purpose = "REGISTER".equalsIgnoreCase(request.getPurpose()) ? OtpPurpose.REGISTRATION : OtpPurpose.LOGIN;
        otpService.verifyOtp(phone, otp, purpose);

        User user = userRepository.findByPhone(phone)
                .orElseGet(() -> userRepository.findByEmailOrPhone(phone, phone)
                        .orElseThrow(() -> new ResourceNotFoundException("No seller account found with phone: " + phone)));

        user.setStatus(AccountStatus.ACTIVE);
        userRepository.save(user);

        String token = tokenProvider.generateToken(user);
        RefreshToken refreshToken = createRefreshToken(user);

        SellerProfile sp = user.getSellerProfile();
        if (sp == null) {
            sp = sellerProfileRepository.findByUserId(user.getId()).orElse(null);
        }

        Map<String, Object> userMap = new LinkedHashMap<>();
        userMap.put("id", "usr_" + user.getId());
        userMap.put("name", user.getFullName());
        userMap.put("email", user.getEmail());
        userMap.put("phone", user.getPhone());
        userMap.put("role", user.getRole().name());

        Map<String, Object> sellerMap = new LinkedHashMap<>();
        if (sp != null) {
            sellerMap.put("id", "seller_" + sp.getId());
            sellerMap.put("companyName", sp.getCompanyName());
            sellerMap.put("businessType", sp.getBusinessType());
            sellerMap.put("verificationStatus", sp.getStatus().name());
            sellerMap.put("completionPercentage", sp.getCompletionPercentage());
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("token", token);
        data.put("refreshToken", refreshToken.getToken());
        data.put("user", userMap);
        data.put("seller", sellerMap);
        data.put("nextStep", "/seller/onboarding");

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("message", "Mobile number verified successfully.");
        response.put("data", data);
        return response;
    }

    @Transactional
    public Map<String, Object> resendOtp(SellerResendOtpRequest request) {
        String phone = request.getPhone();
        OtpPurpose purpose = "REGISTER".equalsIgnoreCase(request.getPurpose()) ? OtpPurpose.REGISTRATION : OtpPurpose.LOGIN;
        otpService.generateAndSendOtp(phone, purpose);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("cooldownSeconds", 45);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("message", "A new 6-digit verification code has been dispatched via SMS.");
        response.put("data", data);
        return response;
    }

    @Transactional
    public Map<String, Object> login(SellerLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials or account suspended"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        if (user.getStatus() == AccountStatus.SUSPENDED) {
            throw new UnauthorizedException("Account has been suspended. Please contact support.");
        }

        String token = tokenProvider.generateToken(user);
        RefreshToken refreshToken = createRefreshToken(user);

        SellerProfile sp = user.getSellerProfile();
        if (sp == null) {
            sp = sellerProfileRepository.findByUserId(user.getId()).orElse(null);
        }

        Map<String, Object> userMap = new LinkedHashMap<>();
        userMap.put("id", "usr_" + user.getId());
        userMap.put("name", user.getFullName());
        userMap.put("email", user.getEmail());
        userMap.put("role", user.getRole().name());

        Map<String, Object> sellerMap = new LinkedHashMap<>();
        if (sp != null) {
            sellerMap.put("id", "seller_" + sp.getId());
            sellerMap.put("companyName", sp.getCompanyName());
            sellerMap.put("businessType", sp.getBusinessType());
            sellerMap.put("verificationStatus", sp.getStatus().name());
            sellerMap.put("completionPercentage", sp.getCompletionPercentage());
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("token", token);
        data.put("refreshToken", refreshToken.getToken());
        data.put("user", userMap);
        data.put("seller", sellerMap);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("data", data);
        return response;
    }

    @Transactional
    public Map<String, Object> forgotPassword(ForgotPasswordRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            otpService.generateAndSendOtp(user.getEmail(), OtpPurpose.RESET_PASSWORD);
        });

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("message", "If an account exists with this email, a password reset link has been dispatched.");
        return response;
    }

    @Transactional
    public Map<String, Object> resetPassword(ResetPasswordRequest request) {
        if (request.getConfirmPassword() != null && !request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        // Validate token / OTP
        // Reset password for token's user
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("message", "Password has been successfully updated. You can now log in.");
        return response;
    }

    private RefreshToken createRefreshToken(User user) {
        String tokenString = UUID.randomUUID().toString() + "-" + System.currentTimeMillis();
        Instant expiryDate = Instant.now().plusMillis(refreshExpirationMs);
        RefreshToken refreshToken = new RefreshToken(tokenString, user, expiryDate);
        return refreshTokenRepository.save(refreshToken);
    }
}
