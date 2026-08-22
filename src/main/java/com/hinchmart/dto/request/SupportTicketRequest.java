package com.hinchmart.dto.request;

import jakarta.validation.constraints.NotBlank;

public class SupportTicketRequest {

    private String orderId;

    @NotBlank(message = "Subject is required")
    private String subject;

    private String priority = "medium"; // "low", "medium", "high", "urgent"
    private String category = "general"; // "logistics", "billing", "product", "rfq", "general"

    @NotBlank(message = "Message details are required")
    private String message;

    public SupportTicketRequest() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
