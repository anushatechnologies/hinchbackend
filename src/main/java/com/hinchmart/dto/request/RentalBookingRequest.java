package com.hinchmart.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class RentalBookingRequest {

    @NotNull(message = "Rental equipment ID is required")
    private String rentalId; // "rent_jcb_3dx" or numeric string

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer durationDays;
    private String siteAddressId;
    private String siteAddress;
    private Boolean operatorRequired = true;
    private String specialInstructions;

    public RentalBookingRequest() {
    }

    public String getRentalId() {
        return rentalId;
    }

    public void setRentalId(String rentalId) {
        this.rentalId = rentalId;
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

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public Boolean getOperatorRequired() {
        return operatorRequired;
    }

    public void setOperatorRequired(Boolean operatorRequired) {
        this.operatorRequired = operatorRequired;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
}
