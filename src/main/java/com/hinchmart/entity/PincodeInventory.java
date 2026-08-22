package com.hinchmart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pincode_inventory", 
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_product_pincode", columnNames = {"product_id", "pincode"})
    },
    indexes = {
        @Index(name = "idx_pincode_inv_product", columnList = "product_id"),
        @Index(name = "idx_pincode_inv_pincode", columnList = "pincode"),
        @Index(name = "idx_pincode_inv_seller", columnList = "seller_id")
    }
)
public class PincodeInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password"})
    private User seller;

    @Column(name = "pincode", nullable = false, length = 10)
    private String pincode;

    @Column(name = "warehouse_name", length = 150)
    private String warehouseName;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(nullable = false)
    private Integer quantity = 0;

    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity = 0;

    @Column(name = "delivery_days")
    private Integer deliveryDays = 3;

    @Column(name = "min_order_quantity")
    private Integer minOrderQuantity = 1;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public PincodeInventory() {
    }

    public PincodeInventory(Product product, User seller, String pincode, String warehouseName,
                            String city, String state, Integer quantity, Integer deliveryDays) {
        this.product = product;
        this.seller = seller;
        this.pincode = pincode;
        this.warehouseName = warehouseName;
        this.city = city;
        this.state = state;
        this.quantity = quantity != null ? quantity : 0;
        this.reservedQuantity = 0;
        this.deliveryDays = deliveryDays != null ? deliveryDays : 3;
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getAvailableQuantity() {
        return Math.max(0, (this.quantity != null ? this.quantity : 0) - (this.reservedQuantity != null ? this.reservedQuantity : 0));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public Integer getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(Integer deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    public Integer getMinOrderQuantity() {
        return minOrderQuantity;
    }

    public void setMinOrderQuantity(Integer minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
