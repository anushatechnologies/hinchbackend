package com.hinchmart.controller;

import com.hinchmart.dto.request.*;
import com.hinchmart.dto.response.*;
import com.hinchmart.entity.User;
import com.hinchmart.service.AuthService;
import com.hinchmart.service.UserAddressService;
import com.hinchmart.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/user", "/api/users"})
@Tag(name = "Buyer Profile & Jobsite Addresses (Flow 2)", description = "Endpoints for Buyer Profile, Enterprise GSTIN Tax Profile, and Jobsite Delivery Addresses CRUD")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final UserAddressService userAddressService;

    public UserController(UserService userService, AuthService authService, UserAddressService userAddressService) {
        this.userService = userService;
        this.authService = authService;
        this.userAddressService = userAddressService;
    }

    // ==========================================
    // Profile Management Endpoints
    // ==========================================

    @GetMapping("/profile")
    @Operation(summary = "Get Authenticated User Profile", description = "Returns active profile and enterprise details.")
    public ResponseEntity<ApiResponse<UserDto>> getUserProfile(Authentication authentication) {
        User user = authService.getCurrentUser(authentication.getName());
        UserDto profile = userService.getUserProfile(user.getId());
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    @PutMapping("/profile")
    @Operation(summary = "Update User Profile Details", description = "Updates user full name, email, and phone.")
    public ResponseEntity<ApiResponse<UserDto>> updateUserProfile(Authentication authentication,
                                                                  @RequestBody UserProfileUpdateRequest request) {
        User user = authService.getCurrentUser(authentication.getName());
        UserDto updated = userService.updateUserProfile(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", updated));
    }

    @PutMapping("/business-profile")
    @Operation(summary = "Update Enterprise / GSTIN Tax Profile", description = "Updates company name, GSTIN number, industry, and website.")
    public ResponseEntity<ApiResponse<UserDto>> updateBusinessProfile(Authentication authentication,
                                                                      @RequestBody BusinessProfileUpdateRequest request) {
        User user = authService.getCurrentUser(authentication.getName());
        UserDto updated = userService.updateBusinessProfile(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Business profile updated successfully", updated));
    }

    @GetMapping("/profile/buyer")
    @Operation(summary = "Get Current Buyer Profile", description = "Returns buyer profile for the authenticated user.")
    public ResponseEntity<ApiResponse<BuyerProfileDto>> getBuyerProfile(Authentication authentication) {
        User user = authService.getCurrentUser(authentication.getName());
        BuyerProfileDto profile = userService.getBuyerProfile(user.getId());
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    @PutMapping("/profile/buyer")
    @Operation(summary = "Update Buyer Profile", description = "Updates buyer business details, GSTIN, and addresses.")
    public ResponseEntity<ApiResponse<BuyerProfileDto>> updateBuyerProfile(Authentication authentication,
                                                                          @RequestBody BuyerProfileUpdateRequest request) {
        User user = authService.getCurrentUser(authentication.getName());
        BuyerProfileDto updated = userService.updateBuyerProfile(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Buyer profile updated successfully", updated));
    }

    @GetMapping("/profile/seller")
    @Operation(summary = "Get Current Seller Profile", description = "Returns seller profile, warehouse, and KYC status.")
    public ResponseEntity<ApiResponse<SellerProfileDto>> getSellerProfile(Authentication authentication) {
        User user = authService.getCurrentUser(authentication.getName());
        SellerProfileDto profile = userService.getSellerProfile(user.getId());
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    @PutMapping("/profile/seller")
    @Operation(summary = "Update Seller Profile", description = "Updates seller company name, warehouse location, PAN and GSTIN.")
    public ResponseEntity<ApiResponse<SellerProfileDto>> updateSellerProfile(Authentication authentication,
                                                                            @RequestBody SellerProfileUpdateRequest request) {
        User user = authService.getCurrentUser(authentication.getName());
        SellerProfileDto updated = userService.updateSellerProfile(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Seller profile updated successfully", updated));
    }

    // ==========================================
    // Jobsite Delivery Addresses CRUD
    // ==========================================

    @GetMapping("/addresses")
    @Operation(summary = "List Jobsite Delivery Addresses", description = "Returns all jobsite delivery addresses with logistics access specs.")
    public ResponseEntity<ApiResponse<List<UserAddressDto>>> getAddresses(Authentication authentication) {
        User user = authService.getCurrentUser(authentication.getName());
        List<UserAddressDto> addresses = userAddressService.getUserAddresses(user.getId());
        return ResponseEntity.ok(ApiResponse.success(addresses));
    }

    @GetMapping("/addresses/{id}")
    @Operation(summary = "Get Address Details by ID", description = "Returns details for a single jobsite address.")
    public ResponseEntity<ApiResponse<UserAddressDto>> getAddressById(Authentication authentication,
                                                                      @PathVariable Long id) {
        User user = authService.getCurrentUser(authentication.getName());
        UserAddressDto address = userAddressService.getAddressById(user.getId(), id);
        return ResponseEntity.ok(ApiResponse.success(address));
    }

    @PostMapping("/addresses")
    @Operation(summary = "Create Jobsite Delivery Address", description = "Creates a new delivery address with trailer/crane access specifications.")
    public ResponseEntity<ApiResponse<UserAddressDto>> createAddress(Authentication authentication,
                                                                     @Valid @RequestBody UserAddressRequest request) {
        User user = authService.getCurrentUser(authentication.getName());
        UserAddressDto created = userAddressService.createAddress(user.getId(), request);
        return new ResponseEntity<>(ApiResponse.success("Jobsite address added successfully", created), HttpStatus.CREATED);
    }

    @PutMapping("/addresses/{id}")
    @Operation(summary = "Update Jobsite Delivery Address", description = "Updates an existing delivery address.")
    public ResponseEntity<ApiResponse<UserAddressDto>> updateAddress(Authentication authentication,
                                                                     @PathVariable Long id,
                                                                     @Valid @RequestBody UserAddressRequest request) {
        User user = authService.getCurrentUser(authentication.getName());
        UserAddressDto updated = userAddressService.updateAddress(user.getId(), id, request);
        return ResponseEntity.ok(ApiResponse.success("Jobsite address updated successfully", updated));
    }

    @DeleteMapping("/addresses/{id}")
    @Operation(summary = "Delete Jobsite Delivery Address", description = "Removes a delivery address.")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(Authentication authentication,
                                                           @PathVariable Long id) {
        User user = authService.getCurrentUser(authentication.getName());
        userAddressService.deleteAddress(user.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Jobsite address deleted successfully", null));
    }
}

