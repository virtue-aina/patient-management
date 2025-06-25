package dev.virtue.pm.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import dev.virtue.pm.billingservice.model.BillingAccount;
import dev.virtue.pm.billingservice.repository.BillingAccountRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
@RequiredArgsConstructor
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);
    private final BillingAccountRepository billingAccountRepository;

    @Override
    public void createBillingAccount(BillingRequest billingRequest,
                                     StreamObserver<BillingResponse> responseObserver) {

        log.info("createBillingAccount request received {}", billingRequest.toString());

            if (billingAccountRepository.existsByPatientId(billingRequest.getPatientId())) {
                log.warn("Patient with ID {} already has a billing account", billingRequest.getPatientId());
                responseObserver.onError(Status.ALREADY_EXISTS
                        .withDescription("Patient already has a billing account")
                        .asRuntimeException());
                return;
            }

            if (billingAccountRepository.existsByEmail(billingRequest.getEmail())) {
                log.warn("Email {} is already in use", billingRequest.getEmail());
                responseObserver.onError(Status.ALREADY_EXISTS
                        .withDescription("Email is already in use")
                        .asRuntimeException());
                return;
            }

            BillingAccount account = new BillingAccount(
                    billingRequest.getPatientId(),
                    billingRequest.getName(),
                    billingRequest.getEmail()
            );

            BillingAccount savedAccount = billingAccountRepository.save(account);
            log.info("Created billing account with ID: {}", savedAccount.getId());

            BillingResponse response = BillingResponse.newBuilder()
                    .setAccountId(savedAccount.getId().toString())
                    .setStatus(savedAccount.getStatus())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();


    }

}
