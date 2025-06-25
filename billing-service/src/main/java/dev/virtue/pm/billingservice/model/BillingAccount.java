package dev.virtue.pm.billingservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Model representing a billing account in the system.
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BillingAccount {
    private UUID id;
    private String patientId;
    private String name;
    private String email;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
