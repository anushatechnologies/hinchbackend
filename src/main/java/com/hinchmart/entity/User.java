package com.hinchmart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hinchmart.entity.enums.AccountStatus;
import com.hinchmart.entity.enums.Role;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_user_email", columnList = "email"),
    @Index(name = "idx_user_phone", columnList = "phone")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(unique = true, length = 20)
    private String phone;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        indexes = {
            @Index(name = "idx_user_roles_user_id", columnList = "user_id"),
            @Index(name = "idx_user_roles_role", columnList = "role")
        }
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    private Set<Role> roles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AccountStatus status = AccountStatus.ACTIVE;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BuyerProfile buyerProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SellerProfile sellerProfile;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public User() {
    }

    public User(String email, String phone, String password, String fullName, Role role, AccountStatus status) {
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.fullName = fullName;
        if (role != null) {
            this.roles.add(role);
        }
        this.status = status != null ? status : AccountStatus.ACTIVE;
    }

    public User(String email, String phone, String password, String fullName, Set<Role> roles, AccountStatus status) {
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.fullName = fullName;
        if (roles != null) {
            this.roles = new HashSet<>(roles);
        }
        this.status = status != null ? status : AccountStatus.ACTIVE;
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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<Role> getRoles() {
        if (roles == null) {
            roles = new HashSet<>();
        }
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles != null ? new HashSet<>(roles) : new HashSet<>();
    }

    public void addRole(Role role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        if (role != null) {
            this.roles.add(role);
        }
    }

    public void removeRole(Role role) {
        if (this.roles != null && role != null) {
            this.roles.remove(role);
        }
    }

    public boolean hasRole(Role role) {
        return this.roles != null && role != null && this.roles.contains(role);
    }

    public boolean hasAnyRole(Role... checkRoles) {
        if (this.roles == null || checkRoles == null) {
            return false;
        }
        for (Role r : checkRoles) {
            if (r != null && this.roles.contains(r)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Backward-compatible getter returning the primary/highest privilege role.
     */
    public Role getRole() {
        if (roles == null || roles.isEmpty()) {
            return null;
        }
        if (roles.contains(Role.SUPER_ADMIN)) return Role.SUPER_ADMIN;
        if (roles.contains(Role.ADMIN)) return Role.ADMIN;
        if (roles.contains(Role.SELLER_ADMIN)) return Role.SELLER_ADMIN;
        if (roles.contains(Role.SELLER)) return Role.SELLER;
        if (roles.contains(Role.SELLER_STAFF)) return Role.SELLER_STAFF;
        if (roles.contains(Role.BUYER)) return Role.BUYER;
        if (roles.contains(Role.SUPPORT)) return Role.SUPPORT;
        return roles.iterator().next();
    }

    /**
     * Backward-compatible setter for single role assignments.
     */
    public void setRole(Role role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        } else {
            this.roles.clear();
        }
        if (role != null) {
            this.roles.add(role);
        }
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public BuyerProfile getBuyerProfile() {
        return buyerProfile;
    }

    public void setBuyerProfile(BuyerProfile buyerProfile) {
        this.buyerProfile = buyerProfile;
        if (buyerProfile != null) {
            buyerProfile.setUser(this);
        }
    }

    public SellerProfile getSellerProfile() {
        return sellerProfile;
    }

    public void setSellerProfile(SellerProfile sellerProfile) {
        this.sellerProfile = sellerProfile;
        if (sellerProfile != null) {
            sellerProfile.setUser(this);
        }
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
