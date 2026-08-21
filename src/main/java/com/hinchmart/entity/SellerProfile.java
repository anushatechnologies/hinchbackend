package com.hinchmart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hinchmart.entity.enums.SellerStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seller_profiles", indexes = {
    @Index(name = "idx_seller_status", columnList = "status"),
    @Index(name = "idx_seller_gstin", columnList = "gstin")
})
public class SellerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private User user;

    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    @Column(length = 20)
    private String gstin;

    @Column(name = "pan_number", length = 20)
    private String panNumber;

    @Column(name = "business_type", length = 50)
    private String businessType; // Manufacturer, Authorized Distributor, Wholesaler, Stockist, Retailer, Dealer

    @Column(name = "established_year")
    private Integer establishedYear;

    @Column(name = "employee_count", length = 50)
    private String employeeCount; // 1-10, 10-50, 50-100, 100+

    @Column(length = 255)
    private String website;

    @Column(name = "company_email", length = 150)
    private String companyEmail;

    @Column(name = "business_phone", length = 30)
    private String businessPhone;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Address fields
    @Column(length = 60)
    private String country = "India";

    @Column(length = 60)
    private String state;

    @Column(length = 60)
    private String district;

    @Column(length = 60)
    private String city;

    @Column(length = 255)
    private String area;

    @Column(length = 10)
    private String pincode;

    @Column(name = "complete_address", columnDefinition = "TEXT")
    private String completeAddress;

    @Column(name = "warehouse_address", columnDefinition = "TEXT")
    private String warehouseAddress;

    // Legal details
    @Column(length = 50)
    private String cin;

    @Column(name = "trade_license", length = 100)
    private String tradeLicense;

    @Column(length = 50)
    private String msme;

    @Column(name = "rating")
    private Double rating = 4.5;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SellerStatus status = SellerStatus.DRAFT;

    @Column(name = "completion_percentage")
    private Integer completionPercentage = 25;

    // Bank details
    @Column(name = "bank_account_number", length = 35)
    private String bankAccountNumber;

    @Column(name = "bank_ifsc_code", length = 20)
    private String bankIfscCode;

    @Column(name = "bank_name", length = 100)
    private String bankName;

    @Column(name = "bank_account_name", length = 150)
    private String bankAccountName;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @OneToMany(mappedBy = "sellerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SellerDocument> documents = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public SellerProfile() {
    }

    public SellerProfile(User user, String companyName, String gstin, String businessType, SellerStatus status) {
        this.user = user;
        this.companyName = companyName;
        this.gstin = gstin;
        this.businessType = businessType;
        this.status = status != null ? status : SellerStatus.DRAFT;
        this.completionPercentage = 25;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.completionPercentage == null) {
            this.completionPercentage = 25;
        }
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

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Integer getEstablishedYear() {
        return establishedYear;
    }

    public void setEstablishedYear(Integer establishedYear) {
        this.establishedYear = establishedYear;
    }

    public String getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(String employeeCount) {
        this.employeeCount = employeeCount;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCompleteAddress() {
        return completeAddress;
    }

    public void setCompleteAddress(String completeAddress) {
        this.completeAddress = completeAddress;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getTradeLicense() {
        return tradeLicense;
    }

    public void setTradeLicense(String tradeLicense) {
        this.tradeLicense = tradeLicense;
    }

    public String getMsme() {
        return msme;
    }

    public void setMsme(String msme) {
        this.msme = msme;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public SellerStatus getStatus() {
        return status;
    }

    public void setStatus(SellerStatus status) {
        this.status = status;
    }

    public Integer getCompletionPercentage() {
        return completionPercentage != null ? completionPercentage : 25;
    }

    public void setCompletionPercentage(Integer completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankIfscCode() {
        return bankIfscCode;
    }

    public void setBankIfscCode(String bankIfscCode) {
        this.bankIfscCode = bankIfscCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public List<SellerDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<SellerDocument> documents) {
        this.documents = documents;
    }

    public void addDocument(SellerDocument document) {
        documents.add(document);
        document.setSellerProfile(this);
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
