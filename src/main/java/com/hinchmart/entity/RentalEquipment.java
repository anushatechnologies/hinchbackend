package com.hinchmart.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rental_equipment", indexes = {
    @Index(name = "idx_rental_category", columnList = "category"),
    @Index(name = "idx_rental_active", columnList = "is_active")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RentalEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "equipment_code", unique = true, length = 50)
    private String equipmentCode; // e.g. rent_jcb_3dx

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 100)
    private String category; // Earthmoving Equipment, Cranes, Concrete Machinery

    @Column(name = "daily_rate", nullable = false, precision = 12, scale = 2)
    private BigDecimal dailyRate;

    @Column(name = "monthly_rate", precision = 12, scale = 2)
    private BigDecimal monthlyRate;

    @Column(length = 10)
    private String currency = "INR";

    @Column(name = "operator_included", nullable = false)
    private boolean operatorIncluded = true;

    @Column(name = "fuel_terms", length = 100)
    private String fuelTerms = "Fuel extra / Buyer scope";

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "available_units", nullable = false)
    private Integer availableUnits = 1;

    @Column(name = "engine_power", length = 50)
    private String enginePower; // "76 HP"

    @Column(name = "bucket_capacity", length = 50)
    private String bucketCapacity; // "1.1 cu.m"

    @Column(name = "max_dig_depth", length = 50)
    private String maxDigDepth; // "4.77 m"

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public RentalEquipment() {
    }

    public RentalEquipment(String equipmentCode, String name, String category, BigDecimal dailyRate,
                           BigDecimal monthlyRate, String imageUrl, Integer availableUnits,
                           String enginePower, String bucketCapacity, String maxDigDepth) {
        this.equipmentCode = equipmentCode;
        this.name = name;
        this.category = category;
        this.dailyRate = dailyRate;
        this.monthlyRate = monthlyRate;
        this.imageUrl = imageUrl;
        this.availableUnits = availableUnits;
        this.enginePower = enginePower;
        this.bucketCapacity = bucketCapacity;
        this.maxDigDepth = maxDigDepth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEquipmentCode() {
        return equipmentCode != null ? equipmentCode : "rent_" + id;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    public BigDecimal getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(BigDecimal monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isOperatorIncluded() {
        return operatorIncluded;
    }

    public void setOperatorIncluded(boolean operatorIncluded) {
        this.operatorIncluded = operatorIncluded;
    }

    public String getFuelTerms() {
        return fuelTerms;
    }

    public void setFuelTerms(String fuelTerms) {
        this.fuelTerms = fuelTerms;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getAvailableUnits() {
        return availableUnits;
    }

    public void setAvailableUnits(Integer availableUnits) {
        this.availableUnits = availableUnits;
    }

    public String getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(String enginePower) {
        this.enginePower = enginePower;
    }

    public String getBucketCapacity() {
        return bucketCapacity;
    }

    public void setBucketCapacity(String bucketCapacity) {
        this.bucketCapacity = bucketCapacity;
    }

    public String getMaxDigDepth() {
        return maxDigDepth;
    }

    public void setMaxDigDepth(String maxDigDepth) {
        this.maxDigDepth = maxDigDepth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
