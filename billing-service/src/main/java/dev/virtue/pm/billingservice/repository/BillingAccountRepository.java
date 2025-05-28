package dev.virtue.pm.billingservice.repository;

import dev.virtue.pm.billingservice.model.BillingAccount;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory repository for billing accounts.
 */
@Repository
public class BillingAccountRepository {
    
    private final Map<UUID, BillingAccount> accounts = new ConcurrentHashMap<>();
    
    /**
     * Save a billing account.
     * 
     * @param account The account to save
     * @return The saved account with generated ID
     */
    public BillingAccount save(BillingAccount account) {
        if (account.getId() == null) {
            account.setId(UUID.randomUUID());
        }
        accounts.put(account.getId(), account);
        return account;
    }
    
    /**
     * Find a billing account by ID.
     * 
     * @param id The account ID
     * @return The account, or null if not found
     */
    public BillingAccount findById(UUID id) {
        return accounts.get(id);
    }
    
    /**
     * Check if an account exists for the given patient ID.
     * 
     * @param patientId The patient ID
     * @return true if an account exists, false otherwise
     */
    public boolean existsByPatientId(String patientId) {
        return accounts.values().stream()
                .anyMatch(account -> account.getPatientId().equals(patientId));
    }
    
    /**
     * Check if an account exists for the given email.
     * 
     * @param email The email address
     * @return true if an account exists, false otherwise
     */
    public boolean existsByEmail(String email) {
        return accounts.values().stream()
                .anyMatch(account -> account.getEmail().equals(email));
    }
}