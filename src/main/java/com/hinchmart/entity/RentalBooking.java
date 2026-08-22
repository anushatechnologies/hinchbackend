package com.hinchmart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rental_bookings", indexes = {
    @Index(name = "idx_booking_user", columnList = "user_id"),
    @Index(name = "idx_booking_equipment", columnList = "equipment_id")
})
public class RentalBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_number", unique = true, length = 50)
    private String bookingNumber; // e.g. BK-RENT-99210

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipment_id", nullable = false)
    private RentalEquipment equipment;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;

    @Column(name = "site_address_id", length = 50)
    private String siteAddressId;

    @Column(name = "site_address_details", columnDefinition = "TEXT")
    private String siteAddressDetails;

    @Column(name = "operator_required", nullable = false)
    private boolean operatorRequired = true;

    @Column(name = "special_instructions", columnDefinition = "TEXT")
    private String specialInstructions;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(length = 30, nullable = false)
    private String status = "CONFIRMED"; // "CONFIRMED", "IN_USE", "COMPLETED", "CANCELLED"

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public RentalBooking() {
    }

    public RentalBooking(String bookingNumber, User user, RentalEquipment equipment, LocalDate startDate,
                         LocalDate endDate, Integer durationDays, String siteAddressId,
                         boolean operatorRequired, String specialInstructions, BigDecimal totalAmount) {
        this.bookingNumber = bookingNumber;
        this.user = user;
        this.equipment = equipment;
        this.startDate = startDate;
        this.endDate = endDate;
        this.durationDays = durationDays;
        this.siteAddressId = siteAddressId;
        this.operatorRequired = operatorRequired;
        this.specialInstructions = specialInstructions;
        this.totalAmount = totalAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingNumber() {
        return bookingNumber != null ? bookingNumber : "BK-RENT-" + id;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RentalEquipment getEquipment() {
        return equipment;
    }

    public void setEquipment(RentalEquipment equipment) {
        this.equipment = equipment;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public String getSiteAddressId() {
        return siteAddressId;
    }

    public void setSiteAddressId(String siteAddressId) {
        this.siteAddressId = siteAddressId;
    }

    public String getSiteAddressDetails() {
        return siteAddressDetails;
    }

    public void setSiteAddressDetails(String siteAddressDetails) {
        this.siteAddressDetails = siteAddressDetails;
    }

    public boolean isOperatorRequired() {
        return operatorRequired;
    }

    public void setOperatorRequired(boolean operatorRequired) {
        this.operatorRequired = operatorRequired;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
