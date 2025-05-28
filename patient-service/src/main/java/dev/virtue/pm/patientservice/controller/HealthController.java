package dev.virtue.pm.patientservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for health check endpoints.
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    /**
     * Simple health check endpoint.
     * 
     * @return 200 OK with status information
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");
        healthStatus.put("service", "patient-service");
        healthStatus.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(healthStatus);
    }
    
    /**
     * Detailed health check endpoint with component status.
     * 
     * @return 200 OK with detailed status information
     */
    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> healthCheckDetails() {
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");
        healthStatus.put("service", "patient-service");
        healthStatus.put("timestamp", System.currentTimeMillis());
        
        // Add component status
        Map<String, Object> components = new HashMap<>();
        
        // Database status - in a real application, this would check the actual database connection
        Map<String, Object> database = new HashMap<>();
        database.put("status", "UP");
        database.put("responseTime", "10ms");
        components.put("database", database);
        
        // Add more components as needed
        
        healthStatus.put("components", components);
        
        return ResponseEntity.ok(healthStatus);
    }
}