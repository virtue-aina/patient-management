package dev.virtue.pm.billingservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = {
    "server.port=0",
    "grpc.server.port=0"
})
class BillingServiceApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("[DEBUG_LOG] Testing context loads");
    }

}
