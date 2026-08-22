package com.hinchmart.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckoutPreviewDto {

    private BigDecimal subtotal;
    private BigDecimal discount = BigDecimal.ZERO;
    private BigDecimal shippingCost = BigDecimal.ZERO;
    private BigDecimal deliveryCharge = BigDecimal.ZERO;
    private BigDecimal taxableAmount;
    private Map<String, Object> taxBreakdown = new LinkedHashMap<>();
    private BigDecimal gst;
    private BigDecimal total;
    private BigDecimal grandTotal;
    private String currency = "INR";
    private List<CartItemDto> items = new ArrayList<>();

    public CheckoutPreviewDto() {
    }

    public CheckoutPreviewDto(BigDecimal subtotal, BigDecimal gst, BigDecimal deliveryCharge, BigDecimal total) {
        this.subtotal = subtotal;
        this.gst = gst;
        this.deliveryCharge = deliveryCharge;
        this.shippingCost = deliveryCharge;
        this.total = total;
        this.grandTotal = total;
        this.taxableAmount = subtotal;
        
        // Populate standard 18% GST breakdown (9% CGST + 9% SGST)
        BigDecimal halfGst = gst != null ? gst.divide(BigDecimal.valueOf(2), 2, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO;
        this.taxBreakdown.put("cgst", halfGst);
        this.taxBreakdown.put("sgst", halfGst);
        this.taxBreakdown.put("igst", BigDecimal.ZERO);
        this.taxBreakdown.put("totalTax", gst);
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getShippingCost() {
        return shippingCost != null ? shippingCost : deliveryCharge;
    }

    public void setShippingCost(BigDecimal shippingCost) {
        this.shippingCost = shippingCost;
        this.deliveryCharge = shippingCost;
    }

    public BigDecimal getDeliveryCharge() {
        return deliveryCharge != null ? deliveryCharge : shippingCost;
    }

    public void setDeliveryCharge(BigDecimal deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
        this.shippingCost = deliveryCharge;
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount != null ? taxableAmount : (subtotal != null && discount != null ? subtotal.subtract(discount) : subtotal);
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public Map<String, Object> getTaxBreakdown() {
        return taxBreakdown;
    }

    public void setTaxBreakdown(Map<String, Object> taxBreakdown) {
        this.taxBreakdown = taxBreakdown;
    }

    public BigDecimal getGst() {
        return gst;
    }

    public void setGst(BigDecimal gst) {
        this.gst = gst;
    }

    public BigDecimal getTotal() {
        return total != null ? total : grandTotal;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
        this.grandTotal = total;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal != null ? grandTotal : total;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
        this.total = grandTotal;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<CartItemDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
    }
}

