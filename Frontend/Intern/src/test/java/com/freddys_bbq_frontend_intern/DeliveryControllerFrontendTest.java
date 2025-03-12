package com.freddys_bbq_frontend_intern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freddys_bbq_frontend_intern.model.Delivery;
import com.freddys_bbq_frontend_intern.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryControllerFrontendTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DeliveryControllerFrontend deliveryControllerFrontend;

    @Value("${DELIVERY_BACKEND_URL:http://localhost:8081}")
    private String deliveryBackendUrl;

    private UUID orderId;
    private Delivery[] deliveries;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        deliveries = new Delivery[]{new Delivery(new Order()), new Delivery(new Order())};
        objectMapper = new ObjectMapper();
    }

    /**
     * Test getDeliveries()
     */
    @Test
    void shouldReturnDeliveriesSuccessfully() throws Exception {
        String deliveriesJson = objectMapper.writeValueAsString(deliveries);
        when(restTemplate.getForEntity(deliveryBackendUrl + "/api/delivery/delivery", String.class))
                .thenReturn(ResponseEntity.ok(deliveriesJson));

        ResponseEntity<?> response = deliveryControllerFrontend.getDeliveries();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(Delivery[].class);
        assertThat((Delivery[]) response.getBody()).hasSize(2);
    }

    /**
     * Test getDeliveries() Backend not reachable
     */
    @Test
    void shouldHandleDeliveryFetchingError() {
        when(restTemplate.getForEntity(deliveryBackendUrl + "/api/delivery/delivery", String.class))
                .thenThrow(new RuntimeException("Backend unreachable"));

        ResponseEntity<?> response = deliveryControllerFrontend.getDeliveries();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(response.getBody()).isEqualTo("Error fetching deliveries");
    }

    /**
     * Test getDeliverySite()
     */
    @Test
    void shouldReturnDeliveryPage() {
        String viewName = deliveryControllerFrontend.getDeliverySite();

        assertThat(viewName).isEqualTo("delivery");
    }

    /**
     * Test startDelivery(UUID id)
     */
    @Test
    void shouldStartDeliverySuccessfully() {
        when(restTemplate.postForEntity(deliveryBackendUrl + "/api/delivery/delivery/start", orderId, String.class))
                .thenReturn(ResponseEntity.ok("Lieferung gestartet"));

        ResponseEntity<String> response = deliveryControllerFrontend.startDelivery(orderId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Lieferung gestartet");
    }

    /**
     * Test startDelivery(UUID id), delivery not found
     */
    @Test
    void shouldReturnNotFoundIfDeliveryDoesNotExist() {
        when(restTemplate.postForEntity(deliveryBackendUrl + "/api/delivery/delivery/start", orderId, String.class))
                .thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<String> response = deliveryControllerFrontend.startDelivery(orderId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
