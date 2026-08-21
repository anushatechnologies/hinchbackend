package com.hinchmart.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hinchmart.entity.enums.AccountStatus;
import com.hinchmart.entity.enums.Role;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long id;
    private String phone;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String accountType; // "individual", "business", "contractor", "vendor"

    @JsonProperty("isVerified")
    private boolean isVerified = true;

    @JsonProperty("isPhoneVerified")
    private boolean isPhoneVerified = true;

    @JsonProperty("isEmailVerified")
    private boolean isEmailVerified = true;

    private BusinessProfileDto businessProfile;

    private Role role;
    private AccountStatus status;
    private BuyerProfileDto buyerProfile;
    private SellerProfileDto sellerProfile;
    private LocalDateTime createdAt;

    public UserDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isPhoneVerified() {
        return isPhoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        isPhoneVerified = phoneVerified;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public BusinessProfileDto getBusinessProfile() {
        return businessProfile;
    }

    public void setBusinessProfile(BusinessProfileDto businessProfile) {
        this.businessProfile = businessProfile;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public BuyerProfileDto getBuyerProfile() {
        return buyerProfile;
    }

    public void setBuyerProfile(BuyerProfileDto buyerProfile) {
        this.buyerProfile = buyerProfile;
    }

    public SellerProfileDto getSellerProfile() {
        return sellerProfile;
    }

    public void setSellerProfile(SellerProfileDto sellerProfile) {
        this.sellerProfile = sellerProfile;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
