package com.hinchmart.dto.response;

import com.hinchmart.entity.enums.AccountStatus;
import com.hinchmart.entity.enums.Role;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BuyerDto {

    private Long id;
    private Long userId;
    private Long buyerProfileId;
    private String fullName;
    private String email;
    private String phone;
    private String companyName;
    private String gstin;
    private String businessType;
    private String billingAddress;
    private String shippingAddress;
    private String city;
    private String state;
    private String pincode;
    private BigDecimal creditLimit;
    private BigDecimal annualTurnover;
    private long totalOrders;
    private BigDecimal lifetimeSpend;
    private AccountStatus status;
    private Role role;
    private BuyerProfileDto buyerProfile;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BuyerDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBuyerProfileId() {
        return buyerProfileId;
    }

    public void setBuyerProfileId(Long buyerProfileId) {
        this.buyerProfileId = buyerProfileId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getAnnualTurnover() {
        return annualTurnover;
    }

    public void setAnnualTurnover(BigDecimal annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getLifetimeSpend() {
        return lifetimeSpend;
    }

    public void setLifetimeSpend(BigDecimal lifetimeSpend) {
        this.lifetimeSpend = lifetimeSpend;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public BuyerProfileDto getBuyerProfile() {
        return buyerProfile;
    }

    public void setBuyerProfile(BuyerProfileDto buyerProfile) {
        this.buyerProfile = buyerProfile;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
