package dev.virtue.pm.billingservice.grpc;
import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import dev.virtue.pm.billingservice.model.BillingAccount;
import dev.virtue.pm.billingservice.repository.BillingAccountRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Pattern;

@GrpcService
// TODO: Implement metrics collection for monitoring service performance
// TODO: Consider adding circuit breaker pattern for resilience
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private final BillingAccountRepository billingAccountRepository;

    @Autowired
    public BillingGrpcService(BillingAccountRepository billingAccountRepository) {
        this.billingAccountRepository = billingAccountRepository;
    }

    @Override
    public void createBillingAccount(BillingRequest billingRequest,
                                     StreamObserver<BillingResponse> responseObserver) {

        log.info("createBillingAccount request received {}", billingRequest.toString());

        try {
            // Validate input parameters
            validateRequest(billingRequest, responseObserver);

            // Check if patient already has a billing account
            if (billingAccountRepository.existsByPatientId(billingRequest.getPatientId())) {
                log.warn("Patient with ID {} already has a billing account", billingRequest.getPatientId());
                responseObserver.onError(Status.ALREADY_EXISTS
                        .withDescription("Patient already has a billing account")
                        .asRuntimeException());
                return;
            }

            // Check if email is already in use
            if (billingAccountRepository.existsByEmail(billingRequest.getEmail())) {
                log.warn("Email {} is already in use", billingRequest.getEmail());
                responseObserver.onError(Status.ALREADY_EXISTS
                        .withDescription("Email is already in use")
                        .asRuntimeException());
                return;
            }

            // Create and save the billing account
            BillingAccount account = new BillingAccount(
                    billingRequest.getPatientId(),
                    billingRequest.getName(),
                    billingRequest.getEmail()
            );

            BillingAccount savedAccount = billingAccountRepository.save(account);
            log.info("Created billing account with ID: {}", savedAccount.getId());

            // Build and send the response
            BillingResponse response = BillingResponse.newBuilder()
                    .setAccountId(savedAccount.getId().toString())
                    .setStatus(savedAccount.getStatus())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Error creating billing account", e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error: " + e.getMessage())
                    .asRuntimeException());
        }
    }

    /**
     * Validates the billing request.
     * 
     * @param request The billing request to validate
     * @param responseObserver The response observer to send errors to
     * @return true if valid, false otherwise
     */
    private boolean validateRequest(BillingRequest request, StreamObserver<BillingResponse> responseObserver) {
        // Validate patient ID
        if (request.getPatientId() == null || request.getPatientId().trim().isEmpty()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Patient ID is required")
                    .asRuntimeException());
            return false;
        }

        // Validate name
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Name is required")
                    .asRuntimeException());
            return false;
        }

        // Validate email
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Email is required")
                    .asRuntimeException());
            return false;
        }

        // Validate email format
        if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid email format")
                    .asRuntimeException());
            return false;
        }

        return true;
    }
}
