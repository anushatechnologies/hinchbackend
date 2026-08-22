package com.hinchmart.controller;

import com.hinchmart.dto.request.PincodeInventoryRequest;
import com.hinchmart.dto.response.ApiResponse;
import com.hinchmart.dto.response.PincodeInventoryDto;
import com.hinchmart.entity.User;
import com.hinchmart.service.AuthService;
import com.hinchmart.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Inventory & Pincode Serviceability", description = "Endpoints for multi-pincode inventory management and buyer stock availability verification")
public class InventoryController {

    private final InventoryService inventoryService;
    private final AuthService authService;

    public InventoryController(InventoryService inventoryService, AuthService authService) {
        this.inventoryService = inventoryService;
        this.authService = authService;
    }

    // =========================================================================
    // 1. Seller & Admin Multi-Pincode Inventory Management Endpoints
    // =========================================================================

    @PostMapping("/api/seller/inventory/pincode")
    @Operation(summary = "Add or Update Pincode Inventory",
            description = "Adds or updates stock for a SKU at a specific pincode and automatically syncs total product stock in products and inventory tables.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<PincodeInventoryDto>> addOrUpdatePincodeInventory(
            Authentication authentication,
            @Valid @RequestBody PincodeInventoryRequest request) {
        User user = authService.getCurrentUser(authentication.getName());
        PincodeInventoryDto dto = inventoryService.addOrUpdatePincodeInventory(user.getId(), request);
        return new ResponseEntity<>(ApiResponse.success("Pincode inventory saved and stock synchronized successfully", dto), HttpStatus.OK);
    }

    @PostMapping("/api/seller/inventory/pincode/bulk")
    @Operation(summary = "Bulk Add/Update Pincode Inventories",
            description = "Bulk updates stock across multiple pincodes for one or more SKUs.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<PincodeInventoryDto>>> bulkAddOrUpdatePincodeInventory(
            Authentication authentication,
            @Valid @RequestBody List<PincodeInventoryRequest> requests) {
        User user = authService.getCurrentUser(authentication.getName());
        List<PincodeInventoryDto> dtoList = inventoryService.bulkAddOrUpdatePincodeInventory(user.getId(), requests);
        return ResponseEntity.ok(ApiResponse.success("Bulk pincode inventories updated successfully", dtoList));
    }

    @GetMapping("/api/seller/inventory/pincode/product/{productId}")
    @Operation(summary = "Get Pincode Inventories by Product ID",
            description = "Retrieves all pincode-wise inventory allocations for a given Product ID.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<PincodeInventoryDto>>> getPincodeInventoriesByProductId(
            @PathVariable Long productId) {
        List<PincodeInventoryDto> list = inventoryService.getPincodeInventoriesByProductId(productId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/api/seller/inventory/pincode/sku/{sku}")
    @Operation(summary = "Get Pincode Inventories by SKU",
            description = "Retrieves all pincode-wise inventory allocations for a given SKU code.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<PincodeInventoryDto>>> getPincodeInventoriesBySku(
            @PathVariable String sku) {
        List<PincodeInventoryDto> list = inventoryService.getPincodeInventoriesBySku(sku);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @DeleteMapping("/api/seller/inventory/pincode")
    @Operation(summary = "Delete Pincode Inventory",
            description = "Removes inventory allocation for a specific product and pincode, updating total stock.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePincodeInventory(
            Authentication authentication,
            @RequestParam Long productId,
            @RequestParam String pincode) {
        User user = authService.getCurrentUser(authentication.getName());
        inventoryService.deletePincodeInventory(user.getId(), productId, pincode);
        return ResponseEntity.ok(ApiResponse.success("Pincode inventory removed and total stock synchronized", null));
    }

    // =========================================================================
    // 2. Buyer & Public Stock Availability & Delivery SLA Endpoint
    // =========================================================================

    @GetMapping("/api/inventory/check-availability")
    @Operation(summary = "Check SKU Availability by Pincode",
            description = "Checks real-time available stock, estimated delivery days, and warehouse serviceability for a delivery pincode. Requires authentication (any role).")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER', 'ADMIN', 'SUPER_ADMIN', 'SUPPORT')")
    public ResponseEntity<ApiResponse<PincodeInventoryDto>> checkAvailability(
            @RequestParam String skuOrId,
            @RequestParam String pincode,
            @RequestParam(required = false, defaultValue = "1") Integer quantity) {
        PincodeInventoryDto dto = inventoryService.checkPincodeAvailability(skuOrId, pincode, quantity);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @GetMapping("/api/inventory/pincode/{pincode}")
    @Operation(summary = "Get All SKUs Available in a Specific Pincode",
            description = "Returns all active SKUs, available quantities, and warehouses serving a specific pincode, with optional category filtering. Requires authentication (any role).")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER', 'ADMIN', 'SUPER_ADMIN', 'SUPPORT')")
    public ResponseEntity<ApiResponse<List<PincodeInventoryDto>>> getInventoriesByPincode(
            @PathVariable String pincode,
            @RequestParam(required = false) Long categoryId) {
        List<PincodeInventoryDto> list = inventoryService.getInventoriesByPincodeAndCategory(pincode, categoryId);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/api/inventory/search")
    @Operation(summary = "Search Inventory by Any Combination of Filters",
            description = "Search inventory across warehouses by pincode, category, subcategory, brand, keyword, and stock status. Requires authentication (any role).")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAnyRole('BUYER', 'SELLER', 'ADMIN', 'SUPER_ADMIN', 'SUPPORT')")
    public ResponseEntity<ApiResponse<List<PincodeInventoryDto>>> searchInventory(
            @RequestParam(required = false) String pincode,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long subcategoryId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false, defaultValue = "false") Boolean inStockOnly) {
        List<PincodeInventoryDto> list = inventoryService.searchPincodeInventory(
                pincode, categoryId, subcategoryId, brandId, query, inStockOnly
        );
        return ResponseEntity.ok(ApiResponse.success(list));
    }
}
