package com.hinchmart.controller;

import com.hinchmart.dto.request.PaymentCreateRequest;
import com.hinchmart.dto.request.PaymentVerifyRequest;
import com.hinchmart.dto.request.RefundRequest;
import com.hinchmart.dto.response.ApiResponse;
import com.hinchmart.dto.response.PaymentDto;
import com.hinchmart.dto.response.RazorpayConfigDto;
import com.hinchmart.dto.response.RefundDto;
import com.hinchmart.entity.User;
import com.hinchmart.service.AuthService;
import com.hinchmart.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment & Razorpay Gateway Operations", description = "Endpoints for Razorpay Gateway Config, Order Creation, Cryptographic HMAC-SHA256 Signature Verification, Webhooks, and Refunds")
public class PaymentController {

    private final PaymentService paymentService;
    private final AuthService authService;

    public PaymentController(PaymentService paymentService, AuthService authService) {
        this.paymentService = paymentService;
        this.authService = authService;
    }

    @GetMapping("/config")
    @Operation(summary = "Get Razorpay Gateway Configuration", description = "Returns public Razorpay Key ID, default currency, and business brand name for client checkout integration.")
    public ResponseEntity<ApiResponse<RazorpayConfigDto>> getPaymentConfig() {
        RazorpayConfigDto config = paymentService.getPublicConfig();
        return ResponseEntity.ok(ApiResponse.success(config));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('BUYER', 'ADMIN', 'SUPER_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create Razorpay Payment Order",
            description = "Initializes payment on Razorpay live servers. Automatically recalculates and enforces the exact order total from the database in paise (₹1 = 100 paise).")
    public ResponseEntity<ApiResponse<PaymentDto>> createPayment(Authentication authentication,
                                                                 @Valid @RequestBody PaymentCreateRequest request) {
        User user = authService.getCurrentUser(authentication.getName());
        PaymentDto payment = paymentService.createPayment(user.getId(), request);
        return new ResponseEntity<>(ApiResponse.success("Razorpay payment order initiated successfully", payment), HttpStatus.CREATED);
    }

    @PostMapping("/verify")
    @PreAuthorize("hasAnyRole('BUYER', 'ADMIN', 'SUPER_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Verify Razorpay Payment Signature & Confirm Order",
            description = "Validates cryptographic HMAC-SHA256 signature (order_id|payment_id), transitions Order to PAID & CONFIRMED, generates GST Tax Invoice, and dispatches notifications.")
    public ResponseEntity<ApiResponse<PaymentDto>> verifyPayment(Authentication authentication,
                                                                 @Valid @RequestBody PaymentVerifyRequest request) {
        User user = authService.getCurrentUser(authentication.getName());
        PaymentDto verified = paymentService.verifyPayment(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Payment signature verified successfully", verified));
    }

    @PostMapping("/webhook")
    @Operation(summary = "Razorpay Webhook Callback Handler", description = "Receives and processes asynchronous webhook callbacks from Razorpay (payment.captured, payment.failed, etc.).")
    public ResponseEntity<ApiResponse<String>> handleWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "X-Razorpay-Signature", required = false) String signature) {
        paymentService.handleWebhookEvent(payload, signature);
        return ResponseEntity.ok(ApiResponse.success("Webhook processed successfully", "OK"));
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER', 'ADMIN', 'SUPER_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get Payment Details by Order ID", description = "Returns payment record, transaction history, and refund status for an order.")
    public ResponseEntity<ApiResponse<PaymentDto>> getPaymentByOrderId(Authentication authentication,
                                                                       @PathVariable Long orderId) {
        User user = authService.getCurrentUser(authentication.getName());
        PaymentDto payment = paymentService.getPaymentByOrderId(orderId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(payment));
    }

    @PostMapping("/{id}/refund")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Process Razorpay Payment Refund", description = "Issues a full or partial refund for a successful payment transaction directly via Razorpay API.")
    public ResponseEntity<ApiResponse<RefundDto>> processRefund(Authentication authentication,
                                                                @PathVariable Long id,
                                                                @Valid @RequestBody RefundRequest request) {
        User user = authService.getCurrentUser(authentication.getName());
        RefundDto refund = paymentService.processRefund(id, user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Refund processed successfully via Razorpay", refund));
    }
}
