package com.hinchmart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "support_tickets", indexes = {
    @Index(name = "idx_ticket_user", columnList = "user_id"),
    @Index(name = "idx_ticket_status", columnList = "status")
})
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_number", unique = true, length = 50)
    private String ticketNumber; // e.g. TKT-88421

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "order_id", length = 50)
    private String orderId;

    @Column(nullable = false, length = 200)
    private String subject;

    @Column(length = 50)
    private String priority = "medium"; // "low", "medium", "high", "urgent"

    @Column(length = 50)
    private String category = "general"; // "logistics", "billing", "product", "rfq", "general"

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(length = 30, nullable = false)
    private String status = "OPEN"; // "OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED"

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public SupportTicket() {
    }

    public SupportTicket(String ticketNumber, User user, String orderId, String subject,
                         String priority, String category, String message) {
        this.ticketNumber = ticketNumber;
        this.user = user;
        this.orderId = orderId;
        this.subject = subject;
        this.priority = priority != null ? priority : "medium";
        this.category = category != null ? category : "general";
        this.message = message;
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

    public String getTicketNumber() {
        return ticketNumber != null ? ticketNumber : "TKT-" + id;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
