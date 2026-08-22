package com.hinchmart.service;

import com.hinchmart.dto.request.RentalBookingRequest;
import com.hinchmart.dto.response.RentalBookingDto;
import com.hinchmart.dto.response.RentalEquipmentDto;
import com.hinchmart.entity.RentalBooking;
import com.hinchmart.entity.RentalEquipment;
import com.hinchmart.entity.User;
import com.hinchmart.exception.BadRequestException;
import com.hinchmart.exception.ResourceNotFoundException;
import com.hinchmart.repository.RentalBookingRepository;
import com.hinchmart.repository.RentalEquipmentRepository;
import com.hinchmart.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RentalService {

    private final RentalEquipmentRepository rentalEquipmentRepository;
    private final RentalBookingRepository rentalBookingRepository;
    private final UserRepository userRepository;
    private final ActivityLogService activityLogService;

    public RentalService(RentalEquipmentRepository rentalEquipmentRepository,
                         RentalBookingRepository rentalBookingRepository,
                         UserRepository userRepository,
                         ActivityLogService activityLogService) {
        this.rentalEquipmentRepository = rentalEquipmentRepository;
        this.rentalBookingRepository = rentalBookingRepository;
        this.userRepository = userRepository;
        this.activityLogService = activityLogService;
    }

    @Transactional(readOnly = true)
    public List<RentalEquipmentDto> getAllEquipment() {
        return rentalEquipmentRepository.findByIsActiveTrueOrderByCategoryAscDailyRateAsc()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RentalEquipmentDto getEquipmentById(String identifier) {
        RentalEquipment equipment = findEquipmentByIdentifier(identifier);
        return mapToDto(equipment);
    }

    @Transactional
    public RentalBookingDto bookRental(Long userId, RentalBookingRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        RentalEquipment equipment = findEquipmentByIdentifier(request.getRentalId());

        LocalDate start = request.getStartDate() != null ? request.getStartDate() : LocalDate.now().plusDays(1);
        LocalDate end = request.getEndDate() != null ? request.getEndDate() : start.plusDays(request.getDurationDays() != null ? request.getDurationDays() : 7);

        int durationDays = (int) ChronoUnit.DAYS.between(start, end);
        if (durationDays <= 0) {
            durationDays = 1;
        }

        BigDecimal total = equipment.getDailyRate().multiply(BigDecimal.valueOf(durationDays));

        String bookingNumber = "BK-RENT-" + System.currentTimeMillis() % 1000000;
        RentalBooking booking = new RentalBooking(
                bookingNumber,
                user,
                equipment,
                start,
                end,
                durationDays,
                request.getSiteAddressId() != null ? request.getSiteAddressId() : "addr_1",
                Boolean.TRUE.equals(request.getOperatorRequired()),
                request.getSpecialInstructions(),
                total
        );
        booking.setSiteAddressDetails(request.getSiteAddress());

        RentalBooking saved = rentalBookingRepository.save(booking);
        activityLogService.log(userId, user.getEmail(), "RENTAL_BOOKED", "RENTAL_BOOKING", saved.getId(),
                "Booked equipment: " + equipment.getName() + " for " + durationDays + " days", null);

        return mapToBookingDto(saved);
    }

    @Transactional(readOnly = true)
    public List<RentalBookingDto> getUserBookings(Long userId) {
        return rentalBookingRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToBookingDto)
                .collect(Collectors.toList());
    }

    private RentalEquipment findEquipmentByIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new BadRequestException("Rental equipment identifier is required");
        }
        if (identifier.startsWith("rent_")) {
            return rentalEquipmentRepository.findByEquipmentCode(identifier)
                    .orElseGet(() -> {
                        try {
                            Long num = Long.parseLong(identifier.substring(5));
                            return rentalEquipmentRepository.findById(num)
                                    .orElseThrow(() -> new ResourceNotFoundException("Rental equipment not found: " + identifier));
                        } catch (NumberFormatException e) {
                            throw new ResourceNotFoundException("Rental equipment not found: " + identifier);
                        }
                    });
        }
        try {
            Long num = Long.parseLong(identifier);
            return rentalEquipmentRepository.findById(num)
                    .orElseThrow(() -> new ResourceNotFoundException("Rental equipment not found with ID: " + identifier));
        } catch (NumberFormatException e) {
            return rentalEquipmentRepository.findByEquipmentCode(identifier)
                    .orElseThrow(() -> new ResourceNotFoundException("Rental equipment not found: " + identifier));
        }
    }

    public RentalEquipmentDto mapToDto(RentalEquipment eq) {
        RentalEquipmentDto dto = new RentalEquipmentDto();
        dto.setId(eq.getEquipmentCode());
        dto.setNumericId(eq.getId());
        dto.setName(eq.getName());
        dto.setCategory(eq.getCategory());
        dto.setDailyRate(eq.getDailyRate());
        dto.setMonthlyRate(eq.getMonthlyRate());
        dto.setCurrency(eq.getCurrency());
        dto.setOperatorIncluded(eq.isOperatorIncluded());
        dto.setFuelTerms(eq.getFuelTerms());
        dto.setImageUrl(eq.getImageUrl());
        dto.setAvailableUnits(eq.getAvailableUnits());
        dto.setDescription(eq.getDescription());

        if (eq.getEnginePower() != null) dto.getSpecifications().put("enginePower", eq.getEnginePower());
        if (eq.getBucketCapacity() != null) dto.getSpecifications().put("bucketCapacity", eq.getBucketCapacity());
        if (eq.getMaxDigDepth() != null) dto.getSpecifications().put("maxDigDepth", eq.getMaxDigDepth());

        return dto;
    }

    public RentalBookingDto mapToBookingDto(RentalBooking bk) {
        RentalBookingDto dto = new RentalBookingDto();
        dto.setId("bk_" + bk.getId());
        dto.setBookingNumber(bk.getBookingNumber());
        dto.setEquipment(mapToDto(bk.getEquipment()));
        dto.setStartDate(bk.getStartDate());
        dto.setEndDate(bk.getEndDate());
        dto.setDurationDays(bk.getDurationDays());
        dto.setSiteAddressId(bk.getSiteAddressId());
        dto.setSiteAddressDetails(bk.getSiteAddressDetails());
        dto.setOperatorRequired(bk.isOperatorRequired());
        dto.setSpecialInstructions(bk.getSpecialInstructions());
        dto.setTotalAmount(bk.getTotalAmount());
        dto.setStatus(bk.getStatus());
        dto.setCreatedAt(bk.getCreatedAt());
        return dto;
    }
}
