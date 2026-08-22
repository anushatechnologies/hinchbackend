package com.hinchmart.dto.request;

import java.util.List;

public class CheckoutPreviewRequest {

    private String shippingAddressId;
    private String deliveryMethodId;
    private String couponCode;
    private String shippingAddress;
    private String pincode;
    private List<AddToCartRequest> items; // Optional direct items

    public CheckoutPreviewRequest() {
    }

    public String getShippingAddressId() {
        return shippingAddressId;
    }

    public void setShippingAddressId(String shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public String getDeliveryMethodId() {
        return deliveryMethodId;
    }

    public void setDeliveryMethodId(String deliveryMethodId) {
        this.deliveryMethodId = deliveryMethodId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public List<AddToCartRequest> getItems() {
        return items;
    }

    public void setItems(List<AddToCartRequest> items) {
        this.items = items;
    }
}

