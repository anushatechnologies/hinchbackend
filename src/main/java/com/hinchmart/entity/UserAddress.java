package com.hinchmart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_addresses", indexes = {
    @Index(name = "idx_user_address_user", columnList = "user_id"),
    @Index(name = "idx_user_address_postal_code", columnList = "postal_code")
})
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false, length = 100)
    private String label; // "Main Jobsite - Tower Alpha", "Head Office", etc.

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(name = "alternate_phone", length = 20)
    private String alternatePhone;

    @Column(name = "company_name", length = 150)
    private String companyName;

    @Column(length = 20)
    private String gstin;

    @Column(name = "address_line1", nullable = false, columnDefinition = "TEXT")
    private String addressLine1;

    @Column(name = "address_line2", columnDefinition = "TEXT")
    private String addressLine2;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(length = 100)
    private String country = "India";

    @Column(name = "postal_code", nullable = false, length = 20)
    private String postalCode;

    @Column(name = "is_default", nullable = false)
    @JsonProperty("isDefault")
    private boolean isDefault = false;

    @Column(name = "address_type", length = 30)
    private String addressType = "site"; // "site", "billing", "office", "warehouse"

    @Column(name = "site_access", length = 50)
    private String siteAccess = "heavy_trailer"; // "heavy_trailer", "truck_10w", "tempo_6w", "standard"

    @Column(name = "crane_available", nullable = false)
    private boolean craneAvailable = false;

    @Column(name = "gate_pass_required", nullable = false)
    private boolean gatePassRequired = false;

    @Column(name = "entry_timings", length = 100)
    private String entryTimings = "06:00 AM - 10:00 PM";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public UserAddress() {
    }

    public UserAddress(User user, String label, String fullName, String phone, String addressLine1,
                       String city, String state, String postalCode) {
        this.user = user;
        this.label = label;
        this.fullName = fullName;
        this.phone = phone;
        this.addressLine1 = addressLine1;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAlternatePhone() {
        return alternatePhone;
    }

    public void setAlternatePhone(String alternatePhone) {
        this.alternatePhone = alternatePhone;
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

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getSiteAccess() {
        return siteAccess;
    }

    public void setSiteAccess(String siteAccess) {
        this.siteAccess = siteAccess;
    }

    public boolean isCraneAvailable() {
        return craneAvailable;
    }

    public void setCraneAvailable(boolean craneAvailable) {
        this.craneAvailable = craneAvailable;
    }

    public boolean isGatePassRequired() {
        return gatePassRequired;
    }

    public void setGatePassRequired(boolean gatePassRequired) {
        this.gatePassRequired = gatePassRequired;
    }

    public String getEntryTimings() {
        return entryTimings;
    }

    public void setEntryTimings(String entryTimings) {
        this.entryTimings = entryTimings;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
