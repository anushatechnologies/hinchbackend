package com.hinchmart.controller;

import com.hinchmart.dto.request.SellerAddressUpdateRequest;
import com.hinchmart.dto.request.SellerLegalUpdateRequest;
import com.hinchmart.dto.request.SellerProfileUpdateRequest;
import com.hinchmart.dto.response.SellerOnboardingProfileDto;
import com.hinchmart.entity.User;
import com.hinchmart.service.AuthService;
import com.hinchmart.service.SellerOnboardingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping({"/api/v1/seller", "/api/seller"})
@Tag(name = "Seller Onboarding & KYC", description = "Multi-step onboarding endpoints for Company details, Address, Legal/Tax metadata, Document Uploads, and Verification")
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasAnyRole('SELLER', 'SELLER_ADMIN', 'SELLER_STAFF', 'ADMIN', 'SUPER_ADMIN')")
public class SellerOnboardingController {

    private final SellerOnboardingService sellerOnboardingService;
    private final AuthService authService;

    public SellerOnboardingController(SellerOnboardingService sellerOnboardingService,
                                      AuthService authService) {
        this.sellerOnboardingService = sellerOnboardingService;
        this.authService = authService;
    }

    @GetMapping("/profile")
    @Operation(summary = "Get Seller Profile", description = "Retrieves the authenticated seller's complete profile, address, legal data, verification checklist, and progress.")
    public ResponseEntity<Map<String, Object>> getProfile(Authentication authentication) {
        User user = authService.getCurrentUser(authentication.getName());
        SellerOnboardingProfileDto profile = sellerOnboardingService.getProfile(user.getId());
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("data", profile);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/profile")
    @Operation(summary = "Update Company Details", description = "Updates company info (establishedYear, employees, website, companyEmail, businessPhone, description).")
    public ResponseEntity<Map<String, Object>> updateCompanyProfile(Authentication authentication,
                                                                   @RequestBody SellerProfileUpdateRequest request) {
        User user = authService.getCurrentUser(authentication.getName());
        Map<String, Object> response = sellerOnboardingService.updateCompanyProfile(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/address")
    @Operation(summary = "Update Operational Address", description = "Updates seller country, state, district, city, area, pincode, and completeAddress.")
    public ResponseEntity<Map<String, Object>> updateAddress(Authentication authentication,
                                                             @RequestBody SellerAddressUpdateRequest request) {
        User user = authService.getCurrentUser(authentication.getName());
        Map<String, Object> response = sellerOnboardingService.updateAddress(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/legal")
    @Operation(summary = "Update Legal & Tax Metadata", description = "Updates GSTIN, PAN, CIN, Trade License, MSME, and Bank details.")
    public ResponseEntity<Map<String, Object>> updateLegal(Authentication authentication,
                                                           @RequestBody SellerLegalUpdateRequest request) {
        User user = authService.getCurrentUser(authentication.getName());
        Map<String, Object> response = sellerOnboardingService.updateLegal(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/documents/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload KYC Document", description = "Uploads GST certificate, PAN card, Incorporation certificate, MSME certificate, or Cancelled Cheque.")
    public ResponseEntity<Map<String, Object>> uploadDocument(
            Authentication authentication,
            @RequestParam("documentType") String documentType,
            @RequestParam("file") MultipartFile file) {
        User user = authService.getCurrentUser(authentication.getName());
        Map<String, Object> response = sellerOnboardingService.uploadDocument(user.getId(), documentType, file);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/submit-verification")
    @Operation(summary = "Submit Onboarding Application for Review", description = "Sets verificationStatus to UNDER_REVIEW and marks completion to 100%.")
    public ResponseEntity<Map<String, Object>> submitForVerification(Authentication authentication) {
        User user = authService.getCurrentUser(authentication.getName());
        Map<String, Object> response = sellerOnboardingService.submitForVerification(user.getId());
        return ResponseEntity.ok(response);
    }
}
