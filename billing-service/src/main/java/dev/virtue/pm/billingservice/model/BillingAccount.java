package dev.virtue.pm.billingservice.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Model representing a billing account in the system.
 */
public class BillingAccount {

    private UUID id;
    private String patientId;
    private String name;
    private String email;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Default constructor required by JPA
    public BillingAccount() {
    }

    public BillingAccount(String patientId, String name, String email) {
        this.patientId = patientId;
        this.name = name;
        this.email = email;
        this.status = "ACTIVE";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
