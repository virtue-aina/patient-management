package dev.virtue.pm.billingservice.repository;

import dev.virtue.pm.billingservice.model.BillingAccount;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BillingAccountRepository {
    
    private final Map<UUID, BillingAccount> accounts = new ConcurrentHashMap<>();

    public BillingAccount save(BillingAccount account) {
        if (account.getId() == null) {
            account.setId(UUID.randomUUID());
        }
        accounts.put(account.getId(), account);
        return account;
    }

    public BillingAccount findById(UUID id) {
        return accounts.get(id);
    }

    public boolean existsByPatientId(String patientId) {
        return accounts.values().stream()
                .anyMatch(account -> account.getPatientId().equals(patientId));
    }

    public boolean existsByEmail(String email) {
        return accounts.values().stream()
                .anyMatch(account -> account.getEmail().equals(email));
    }
}