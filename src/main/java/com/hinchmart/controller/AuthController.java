package com.hinchmart.controller;

import com.hinchmart.dto.request.LoginRequest;
import com.hinchmart.dto.request.RefreshTokenRequest;
import com.hinchmart.dto.request.RegisterRequest;
import com.hinchmart.dto.request.SendOtpRequest;
import com.hinchmart.dto.request.VerifyOtpRequest;
import com.hinchmart.dto.response.*;
import com.hinchmart.exception.UnauthorizedException;
import com.hinchmart.service.AuthService;
import com.hinchmart.service.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication & Authorization", description = "Endpoints for Buyer/Seller Registration, Phone OTP, Login, Refresh Token and Session Management")
public class AuthController {

    private final AuthService authService;
    private final OtpService otpService;

    public AuthController(AuthService authService, OtpService otpService) {
        this.authService = authService;
        this.otpService = otpService;
    }

    @PostMapping("/send-otp")
    @Operation(summary = "Send OTP to Phone/Email", description = "Generates and dispatches a 6-digit one-time passcode to mobile phone or email.")
    public ResponseEntity<SendOtpResponse> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        SendOtpResponse response = otpService.sendOtp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP Code", description = "Validates the 6-digit passcode. Returns JWT session tokens for existing users, or verificationToken for new users.")
    public ResponseEntity<VerifyOtpResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        VerifyOtpResponse response = authService.verifyOtp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Complete Registration", description = "Creates a new user profile after phone OTP verification or direct registration.")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("success", true);
        resp.put("message", "Account created successfully");
        resp.put("accessToken", response.getAccessToken());
        resp.put("refreshToken", response.getRefreshToken());
        resp.put("user", response.getUser());
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Login with Password", description = "Authenticates user credentials and returns JWT access & refresh tokens.")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh Access Token", description = "Obtains a new access token using a valid refresh token.")
    public ResponseEntity<Map<String, Object>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("success", true);
        resp.put("accessToken", response.getAccessToken());
        resp.put("refreshToken", response.getRefreshToken());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/me")
    @Operation(summary = "Fetch Active User Session (/me)", description = "Fetches profile details of the currently authenticated user.")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Authentication token is missing or invalid");
        }
        UserDto userDto = authService.getCurrentUserDto(authentication.getName());
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("success", true);
        resp.put("user", userDto);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/logout")
    @Operation(summary = "User Logout", description = "Invalidates the active refresh token and ends the session.")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Map<String, Object>> logout(@RequestBody(required = false) RefreshTokenRequest request) {
        if (request != null && request.getRefreshToken() != null) {
            authService.logout(request.getRefreshToken());
        }
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("success", true);
        resp.put("message", "Logged out successfully");
        return ResponseEntity.ok(resp);
    }
}
