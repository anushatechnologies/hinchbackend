package com.hinchmart.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RentalBookingDto {

    private String id;
    private String bookingNumber;
    private RentalEquipmentDto equipment;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer durationDays;
    private String siteAddressId;
    private String siteAddressDetails;
    private boolean operatorRequired;
    private String specialInstructions;
    private BigDecimal totalAmount;
    private String currency = "INR";
    private String status;
    private LocalDateTime createdAt;

    public RentalBookingDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public RentalEquipmentDto getEquipment() {
        return equipment;
    }

    public void setEquipment(RentalEquipmentDto equipment) {
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
