package com.freddys_bbq_frontend_customer;

import com.freddys_bbq_frontend_customer.model.Delivery;
import com.freddys_bbq_frontend_customer.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private Model model;

    @InjectMocks
    private DeliveryController deliveryController;

    @Value("${DELIVERY_BACKEND_URL:http://localhost:8081}")
    private String deliveryBackendUrl;

    @Value("${INTERN_FRONTEND_URL:http://localhost:4300}")
    private String internFrontendUrl;

    private UUID orderId;
    private Delivery delivery;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        delivery = new Delivery(new Order());
        delivery.setStatus("In Delivery");
    }

    /**
     * Test getDeliveryStatus(UUID id)
     */
    @Test
    void shouldReturnDeliveryStatus() {
        when(restTemplate.getForEntity(deliveryBackendUrl + "/api/delivery/delivery/" + orderId, Delivery.class))
                .thenReturn(ResponseEntity.ok(delivery));

        ResponseEntity<String> response = deliveryController.getDeliveryStatus(orderId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("{\"status\": \"In Delivery\"}");
    }

    /**
     * Test getDeliveryStatus(UUID id), order not found
     */
    @Test
    void shouldReturnNotFoundIfDeliveryDoesNotExist() {
        when(restTemplate.getForEntity(deliveryBackendUrl + "/api/delivery/delivery/" + orderId, Delivery.class))
                .thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<String> response = deliveryController.getDeliveryStatus(orderId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    /**
     * Test getOrder(UUID id, Model model)
     */
    @Test
    void shouldRetrieveOrderSuccessfully() {
        when(restTemplate.getForEntity(deliveryBackendUrl + "/api/delivery/delivery/" + orderId, Delivery.class))
                .thenReturn(ResponseEntity.ok(delivery));

        String viewName = deliveryController.getOrder(orderId, model);

        verify(model).addAttribute("delivery", delivery);
        verify(model, never()).addAttribute(eq("errorMessage"), anyString());
        assertThat(viewName).isEqualTo("order-info");
    }

    /**
     * Test getOrder(UUID id, Model model), order not found
     */
    @Test
    void shouldHandleOrderNotFound() {
        when(restTemplate.getForEntity(deliveryBackendUrl + "/api/delivery/delivery/" + orderId, Delivery.class))
                .thenReturn(ResponseEntity.notFound().build());

        String viewName = deliveryController.getOrder(orderId, model);

        verify(model).addAttribute(eq("delivery"), any(Delivery.class));
        verify(model).addAttribute("errorMessage", "The Order could not be found or the Backend does not answer");
        assertThat(viewName).isEqualTo("order-info");
    }

    /**
     * Test redirectInternDelivery()
     */
    @Test
    void shouldRedirectInternDelivery() {
        ResponseEntity<String> response = deliveryController.redirectInternDelivery();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(internFrontendUrl + "/intern/delivery");
    }
}
