package com.hinchmart.controller;

import com.hinchmart.dto.request.RentalBookingRequest;
import com.hinchmart.dto.response.ApiResponse;
import com.hinchmart.dto.response.RentalBookingDto;
import com.hinchmart.dto.response.RentalEquipmentDto;
import com.hinchmart.entity.User;
import com.hinchmart.service.AuthService;
import com.hinchmart.service.RentalService;
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
@RequestMapping("/api/rentals")
@Tag(name = "Heavy Machinery & Equipment Rentals (Flow 8)", description = "Endpoints for Browsing Equipment Rentals (JCB, Mobile Cranes, Mixers), Rates, and Booking for Construction Sites")
public class RentalController {

    private final RentalService rentalService;
    private final AuthService authService;

    public RentalController(RentalService rentalService, AuthService authService) {
        this.rentalService = rentalService;
        this.authService = authService;
    }

    @GetMapping
    @Operation(summary = "List Available Machinery Rentals", description = "Returns active heavy equipment and machinery available for site hire.")
    public ResponseEntity<ApiResponse<List<RentalEquipmentDto>>> getRentals() {
        List<RentalEquipmentDto> list = rentalService.getAllEquipment();
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Machinery Details & Specs", description = "Returns daily/monthly rental rates, operator terms, and technical specifications.")
    public ResponseEntity<ApiResponse<RentalEquipmentDto>> getRentalById(@PathVariable String id) {
        RentalEquipmentDto dto = rentalService.getEquipmentById(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @PostMapping("/book")
    @PreAuthorize("hasAnyRole('BUYER', 'ADMIN', 'SUPER_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Book Machinery Rental for Jobsite", description = "Submits booking request for heavy machinery with operator option and site address.")
    public ResponseEntity<ApiResponse<RentalBookingDto>> bookRental(Authentication authentication,
                                                                   @Valid @RequestBody RentalBookingRequest request) {
        User user = authService.getCurrentUser(authentication.getName());
        RentalBookingDto booking = rentalService.bookRental(user.getId(), request);
        return new ResponseEntity<>(ApiResponse.success("Machinery booked successfully for your jobsite", booking), HttpStatus.CREATED);
    }

    @GetMapping("/my-bookings")
    @PreAuthorize("hasAnyRole('BUYER', 'ADMIN', 'SUPER_ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "List Buyer Machinery Bookings", description = "Returns all equipment bookings made by current buyer.")
    public ResponseEntity<ApiResponse<List<RentalBookingDto>>> getMyBookings(Authentication authentication) {
        User user = authService.getCurrentUser(authentication.getName());
        List<RentalBookingDto> bookings = rentalService.getUserBookings(user.getId());
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }
}
