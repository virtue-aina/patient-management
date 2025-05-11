package dev.virtue.pm.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
public class BillingGrpcServiceTest {

    private BillingGrpcService billingGrpcService;

    @Mock
    private StreamObserver<BillingResponse> responseObserver;

    @Captor
    private ArgumentCaptor<BillingResponse> responseCaptor;

    @BeforeEach
    public void setUp() {
        billingGrpcService = new BillingGrpcService();
    }

    @Test
    public void testCreateBillingAccount() throws InterruptedException {
        // Set up logger capture
        Logger logger = (Logger) LoggerFactory.getLogger(BillingGrpcService.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);

        // Create a test request
        BillingRequest request = BillingRequest.newBuilder()
                .setPatientId("12345")
                .setName("Joe Rogan")
                .setEmail("joerogue@example.com")
                .build();

        System.out.println("[DEBUG_LOG] About to call createBillingAccount");

        // Set up the mock response observer
        final CountDownLatch latch = new CountDownLatch(1);
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(responseObserver).onCompleted();

        // Call the service method
        billingGrpcService.createBillingAccount(request, responseObserver);

        System.out.println("[DEBUG_LOG] Called createBillingAccount");

        // Wait for the response
        assertTrue(latch.await(5, TimeUnit.SECONDS));

        // Verify the response
        ArgumentCaptor<BillingResponse> responseCaptor = ArgumentCaptor.forClass(BillingResponse.class);
        org.mockito.Mockito.verify(responseObserver).onNext(responseCaptor.capture());
        BillingResponse response = responseCaptor.getValue();

        assertEquals("12345", response.getAccountId());
        assertEquals("ACTIVE", response.getStatus());

        // Verify the log message
        List<ILoggingEvent> logsList = listAppender.list;
        System.out.println("[DEBUG_LOG] Number of log events: " + logsList.size());

        boolean foundLogMessage = false;
        for (ILoggingEvent event : logsList) {
            System.out.println("[DEBUG_LOG] Log event: " + event.getLevel() + " - " + event.getMessage());
            System.out.println("[DEBUG_LOG] Log formatted message: " + event.getFormattedMessage());

            if (event.getLevel() == Level.INFO && 
                event.getMessage().contains("createBillingAccount request received")) {
                foundLogMessage = true;
                break;
            }
        }

        assertTrue(foundLogMessage, "Expected log message not found");
        System.out.println("[DEBUG_LOG] Test completed successfully");
    }
}
