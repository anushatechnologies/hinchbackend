package com.hinchmart.controller;

import com.hinchmart.dto.request.*;
import com.hinchmart.service.SellerAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping({"/api/v1/auth/seller", "/api/auth/seller"})
@Tag(name = "Seller Authentication (H.MART Portal)", description = "Endpoints for Seller Registration, OTP Verification, Resend OTP, Login, and Password Reset")
public class SellerAuthController {

    private final SellerAuthService sellerAuthService;

    public SellerAuthController(SellerAuthService sellerAuthService) {
        this.sellerAuthService = sellerAuthService;
    }

    @PostMapping("/register")
    @Operation(summary = "Seller Registration", description = "Creates a new seller user and draft business profile, then sends mobile verification OTP.")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody SellerRegisterRequest request) {
        Map<String, Object> response = sellerAuthService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify Seller Mobile OTP", description = "Validates the 6-digit passcode. Marks mobile as verified and returns JWT tokens + seller profile.")
    public ResponseEntity<Map<String, Object>> verifyOtp(@Valid @RequestBody SellerVerifyOtpRequest request) {
        Map<String, Object> response = sellerAuthService.verifyOtp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-otp")
    @Operation(summary = "Resend OTP", description = "Dispatches a new 6-digit verification code with rate limiting cooldown.")
    public ResponseEntity<Map<String, Object>> resendOtp(@Valid @RequestBody SellerResendOtpRequest request) {
        Map<String, Object> response = sellerAuthService.resendOtp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Seller Login", description = "Authenticates seller credentials and returns JWT session tokens.")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody SellerLoginRequest request) {
        Map<String, Object> response = sellerAuthService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot Password", description = "Dispatches a password reset link/OTP to registered email.")
    public ResponseEntity<Map<String, Object>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        Map<String, Object> response = sellerAuthService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset Password", description = "Submits a new secure password using reset token / OTP.")
    public ResponseEntity<Map<String, Object>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        Map<String, Object> response = sellerAuthService.resetPassword(request);
        return ResponseEntity.ok(response);
    }
}
