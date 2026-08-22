package com.hinchmart.service;

import com.hinchmart.config.JwtTokenProvider;
import com.hinchmart.dto.request.LoginRequest;
import com.hinchmart.dto.request.RefreshTokenRequest;
import com.hinchmart.dto.request.RegisterRequest;
import com.hinchmart.dto.request.VerifyOtpRequest;
import com.hinchmart.dto.response.*;
import com.hinchmart.entity.BuyerProfile;
import com.hinchmart.entity.RefreshToken;
import com.hinchmart.entity.SellerProfile;
import com.hinchmart.entity.User;
import com.hinchmart.entity.VerificationToken;
import com.hinchmart.entity.enums.AccountStatus;
import com.hinchmart.entity.enums.Role;
import com.hinchmart.entity.enums.SellerStatus;
import com.hinchmart.exception.BadRequestException;
import com.hinchmart.exception.ResourceNotFoundException;
import com.hinchmart.exception.UnauthorizedException;
import com.hinchmart.repository.BuyerProfileRepository;
import com.hinchmart.repository.RefreshTokenRepository;
import com.hinchmart.repository.SellerProfileRepository;
import com.hinchmart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BuyerProfileRepository buyerProfileRepository;
    private final SellerProfileRepository sellerProfileRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final OtpService otpService;
    private final ActivityLogService activityLogService;

    @Value("${hinchmart.jwt.refresh-expiration-ms:2592000000}")
    private long refreshExpirationMs; // 30 days default

    public AuthService(UserRepository userRepository,
                       BuyerProfileRepository buyerProfileRepository,
                       SellerProfileRepository sellerProfileRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider,
                       OtpService otpService,
                       ActivityLogService activityLogService) {
        this.userRepository = userRepository;
        this.buyerProfileRepository = buyerProfileRepository;
        this.sellerProfileRepository = sellerProfileRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.otpService = otpService;
        this.activityLogService = activityLogService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String phone = request.getPhone();

        // 1. If verification token provided (mobile OTP flow), validate and consume it
        if (request.getVerificationToken() != null && !request.getVerificationToken().trim().isEmpty()) {
            VerificationToken vToken = otpService.validateVerificationToken(request.getVerificationToken());
            if (phone == null || phone.trim().isEmpty()) {
                phone = vToken.getPhone();
            }
            otpService.consumeVerificationToken(vToken);
        }

        // 2. Validate email / phone uniqueness
        String email = request.getEmail();
        if (email == null || email.trim().isEmpty()) {
            if (phone != null && !phone.trim().isEmpty()) {
                String cleanPhone = phone.replaceAll("[^0-9]", "");
                email = cleanPhone + "@user.hinchmart.com";
            } else {
                throw new BadRequestException("Email or phone number is required for registration");
            }
        }

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email address is already in use: " + email);
        }

        if (phone != null && !phone.trim().isEmpty() && userRepository.existsByPhone(phone)) {
            throw new BadRequestException("Phone number is already registered: " + phone);
        }

        java.util.Set<Role> roles = request.getRoles();
        if (roles == null || roles.isEmpty()) {
            roles = new java.util.HashSet<>();
            roles.add(request.getRole() != null ? request.getRole() : Role.BUYER);
        }
        String rawPassword = request.getPassword();
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            rawPassword = "User@" + UUID.randomUUID().toString().substring(0, 8);
        }

        String fullName = request.getFullName();

        User user = new User(
                email,
                phone,
                passwordEncoder.encode(rawPassword),
                fullName,
                roles,
                AccountStatus.ACTIVE
        );

        User savedUser = userRepository.save(user);

        // Attach Profile based on Role / AccountType
        boolean isSeller = savedUser.hasAnyRole(Role.SELLER, Role.SELLER_ADMIN, Role.SELLER_STAFF);
        boolean isBuyer = savedUser.hasRole(Role.BUYER) || !isSeller;

        if (isSeller) {
            SellerProfile sellerProfile = new SellerProfile(
                    savedUser,
                    request.getCompanyName() != null ? request.getCompanyName() : fullName + " Trading Co.",
                    request.getGstin(),
                    request.getBusinessType() != null ? request.getBusinessType() : "Distributor",
                    SellerStatus.PENDING
            );
            sellerProfile.setPanNumber(request.getPan());
            sellerProfile.setWarehouseAddress(request.getAddress());
            sellerProfile.setCity(request.getCity());
            sellerProfile.setState(request.getState());
            sellerProfile.setPincode(request.getPincode());
            savedUser.setSellerProfile(sellerProfile);
            sellerProfileRepository.save(sellerProfile);
        }
        if (isBuyer) {
            BuyerProfile buyerProfile = new BuyerProfile(
                    savedUser,
                    request.getCompanyName() != null ? request.getCompanyName() : fullName + " Enterprise",
                    request.getGstin(),
                    request.getBusinessType() != null ? request.getBusinessType() : "Commercial Buyer"
            );
            buyerProfile.setBillingAddress(request.getAddress());
            buyerProfile.setShippingAddress(request.getAddress());
            buyerProfile.setCity(request.getCity());
            buyerProfile.setState(request.getState());
            buyerProfile.setPincode(request.getPincode());
            buyerProfile.setCreditLimit(new BigDecimal("500000.00")); // default credit limit
            savedUser.setBuyerProfile(buyerProfile);
            buyerProfileRepository.save(buyerProfile);
        }

        String accessToken = tokenProvider.generateToken(savedUser);
        RefreshToken refreshToken = createRefreshToken(savedUser);

        activityLogService.log(savedUser.getId(), savedUser.getEmail(), "USER_REGISTERED", "USER", savedUser.getId(),
                "Registered with roles: " + savedUser.getRoles(), null);

        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                tokenProvider.getExpirationMs() / 1000,
                mapToUserDto(savedUser)
        );
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailOrPhone(request.getIdentifier(), request.getIdentifier())
                .orElseThrow(() -> new UnauthorizedException("Invalid email/phone or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email/phone or password");
        }

        if (user.getStatus() == AccountStatus.SUSPENDED) {
            throw new UnauthorizedException("Account has been suspended. Please contact support.");
        }

        if (user.getStatus() == AccountStatus.INACTIVE) {
            throw new UnauthorizedException("Account is inactive. Please verify or activate your account.");
        }

        String accessToken = tokenProvider.generateToken(user);
        RefreshToken refreshToken = createRefreshToken(user);

        activityLogService.log(user.getId(), user.getEmail(), "USER_LOGIN", "USER", user.getId(),
                "Logged in via password authentication", null);

        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                tokenProvider.getExpirationMs() / 1000,
                mapToUserDto(user)
        );
    }

    @Transactional
    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request) {
        String identifier = request.getPhone() != null ? request.getPhone() : request.getIdentifier();
        String otpCode = request.getOtp() != null ? request.getOtp() : request.getOtpCode();

        otpService.verifyOtp(identifier, otpCode, request.getPurpose());

        Optional<User> userOpt = userRepository.findByEmailOrPhone(identifier, identifier);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String accessToken = tokenProvider.generateToken(user);
            RefreshToken refreshToken = createRefreshToken(user);

            activityLogService.log(user.getId(), user.getEmail(), "USER_OTP_LOGIN", "USER", user.getId(),
                    "Logged in via OTP verification", null);

            return VerifyOtpResponse.existingUser(
                    accessToken,
                    refreshToken.getToken(),
                    tokenProvider.getExpirationMs() / 1000,
                    mapToUserDto(user)
            );
        } else {
            String verificationToken = otpService.createVerificationToken(identifier);
            return VerifyOtpResponse.newUser(verificationToken);
        }
    }

    @Transactional
    public AuthResponse verifyOtpAndLogin(VerifyOtpRequest request) {
        String identifier = request.getPhone() != null ? request.getPhone() : request.getIdentifier();
        String otpCode = request.getOtp() != null ? request.getOtp() : request.getOtpCode();

        otpService.verifyOtp(identifier, otpCode, request.getPurpose());

        User user = userRepository.findByEmailOrPhone(identifier, identifier)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for identifier: " + identifier));

        String accessToken = tokenProvider.generateToken(user);
        RefreshToken refreshToken = createRefreshToken(user);

        activityLogService.log(user.getId(), user.getEmail(), "USER_OTP_LOGIN", "USER", user.getId(),
                "Logged in via OTP verification", null);

        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                tokenProvider.getExpirationMs() / 1000,
                mapToUserDto(user)
        );
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken token = refreshTokenRepository.findByTokenAndRevokedFalse(request.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid or revoked refresh token"));

        if (token.getExpiryDate().isBefore(Instant.now())) {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
            throw new UnauthorizedException("Refresh token has expired. Please log in again.");
        }

        User user = token.getUser();
        String newAccessToken = tokenProvider.generateToken(user);

        return new AuthResponse(
                newAccessToken,
                token.getToken(),
                tokenProvider.getExpirationMs() / 1000,
                mapToUserDto(user)
        );
    }

    @Transactional
    public void logout(String refreshTokenStr) {
        if (refreshTokenStr != null && !refreshTokenStr.trim().isEmpty()) {
            refreshTokenRepository.findByToken(refreshTokenStr).ifPresent(token -> {
                token.setRevoked(true);
                refreshTokenRepository.save(token);
            });
        }
    }

    @Transactional(readOnly = true)
    public UserDto getCurrentUserDto(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return mapToUserDto(user);
    }

    @Transactional(readOnly = true)
    public User getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private RefreshToken createRefreshToken(User user) {
        String tokenString = UUID.randomUUID().toString() + "-" + System.currentTimeMillis();
        Instant expiryDate = Instant.now().plusMillis(refreshExpirationMs);

        RefreshToken refreshToken = new RefreshToken(tokenString, user, expiryDate);
        return refreshTokenRepository.save(refreshToken);
    }

    public UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setFullName(user.getFullName());

        // Parse firstName and lastName
        String fullName = user.getFullName() != null ? user.getFullName().trim() : "";
        if (fullName.contains(" ")) {
            String[] parts = fullName.split("\\s+", 2);
            dto.setFirstName(parts[0]);
            dto.setLastName(parts[1]);
        } else {
            dto.setFirstName(fullName);
            dto.setLastName("");
        }

        dto.setRole(user.getRole());
        dto.setRoles(user.getRoles());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());

        // Verification Flags
        dto.setVerified(user.getStatus() == AccountStatus.ACTIVE);
        dto.setPhoneVerified(user.getPhone() != null && !user.getPhone().trim().isEmpty());
        dto.setEmailVerified(user.getEmail() != null && !user.getEmail().trim().isEmpty());

        // Compute Account Type & Business Profile
        if (user.hasAnyRole(Role.SELLER, Role.SELLER_ADMIN, Role.SELLER_STAFF)) {
            dto.setAccountType("vendor");
            if (user.getSellerProfile() != null) {
                SellerProfile sp = user.getSellerProfile();
                SellerProfileDto spDto = new SellerProfileDto();
                spDto.setId(sp.getId());
                spDto.setCompanyName(sp.getCompanyName());
                spDto.setGstin(sp.getGstin());
                spDto.setPanNumber(sp.getPanNumber());
                spDto.setBusinessType(sp.getBusinessType());
                spDto.setWarehouseAddress(sp.getWarehouseAddress());
                spDto.setCity(sp.getCity());
                spDto.setState(sp.getState());
                spDto.setPincode(sp.getPincode());
                spDto.setRating(sp.getRating());
                spDto.setStatus(sp.getStatus());
                spDto.setRejectionReason(sp.getRejectionReason());
                spDto.setVerifiedAt(sp.getVerifiedAt());
                dto.setSellerProfile(spDto);

                BusinessProfileDto bpDto = new BusinessProfileDto(
                        sp.getCompanyName() != null ? sp.getCompanyName() : user.getFullName() + " Trading Co.",
                        sp.getGstin() != null ? sp.getGstin() : "",
                        sp.getPanNumber() != null ? sp.getPanNumber() : (sp.getGstin() != null ? sp.getGstin() : ""),
                        sp.getStatus() == SellerStatus.APPROVED
                );
                dto.setBusinessProfile(bpDto);
            } else {
                dto.setBusinessProfile(new BusinessProfileDto(user.getFullName() + " Trading Co.", "", "", false));
            }
        } else {
            // Buyer Role
            if (user.getBuyerProfile() != null) {
                BuyerProfile bp = user.getBuyerProfile();
                BuyerProfileDto bpDto = new BuyerProfileDto();
                bpDto.setId(bp.getId());
                bpDto.setCompanyName(bp.getCompanyName());
                bpDto.setGstin(bp.getGstin());
                bpDto.setBusinessType(bp.getBusinessType());
                bpDto.setBillingAddress(bp.getBillingAddress());
                bpDto.setShippingAddress(bp.getShippingAddress());
                bpDto.setCity(bp.getCity());
                bpDto.setState(bp.getState());
                bpDto.setPincode(bp.getPincode());
                bpDto.setCreditLimit(bp.getCreditLimit());
                bpDto.setAnnualTurnover(bp.getAnnualTurnover());
                dto.setBuyerProfile(bpDto);

                String bt = bp.getBusinessType() != null ? bp.getBusinessType().toLowerCase() : "business";
                if (bt.contains("contractor") || bt.contains("builder")) {
                    dto.setAccountType("contractor");
                } else if (bt.contains("individual") || bt.contains("retail") || bt.contains("home")) {
                    dto.setAccountType("individual");
                } else {
                    dto.setAccountType("business");
                }

                BusinessProfileDto busDto = new BusinessProfileDto(
                        bp.getCompanyName() != null ? bp.getCompanyName() : user.getFullName() + " Enterprise",
                        bp.getGstin() != null ? bp.getGstin() : "",
                        bp.getGstin() != null ? bp.getGstin() : "",
                        bp.getGstin() != null && !bp.getGstin().trim().isEmpty()
                );
                dto.setBusinessProfile(busDto);
            } else {
                dto.setAccountType("business");
                dto.setBusinessProfile(new BusinessProfileDto(user.getFullName() + " Enterprise", "", "", false));
            }
        }

        return dto;
    }
}
