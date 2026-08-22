package com.hinchmart.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RentalEquipmentDto {

    private String id;
    private Long numericId;
    private String name;
    private String category;
    private BigDecimal dailyRate;
    private BigDecimal monthlyRate;
    private String currency = "INR";
    private boolean operatorIncluded;
    private String fuelTerms;
    private String imageUrl;
    private Integer availableUnits;
    private Map<String, String> specifications = new LinkedHashMap<>();
    private String description;

    public RentalEquipmentDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getNumericId() {
        return numericId;
    }

    public void setNumericId(Long numericId) {
        this.numericId = numericId;
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

    public Map<String, String> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Map<String, String> specifications) {
        this.specifications = specifications;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
