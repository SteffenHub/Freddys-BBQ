package com.freddys_bbq_delivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freddys_bbq_delivery.model.Delivery;
import com.freddys_bbq_delivery.model.MenuItem;
import com.freddys_bbq_delivery.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DeliveryControllerBackendTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private DeliveryControllerBackend deliveryControllerBackend;

    private MockMvc mockMvc;
    private UUID orderId;
    private Delivery delivery;
    private Order order;
    private MenuItem drink;
    private MenuItem side;
    private MenuItem meal;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(deliveryControllerBackend).build();

        order = new Order();
        orderId = UUID.randomUUID();
        order.setId(orderId);

        drink = new MenuItem();
        drink.setId(UUID.randomUUID());
        order.addItem(drink);

        meal = new MenuItem();
        meal.setId(UUID.randomUUID());
        order.addItem(meal);

        side = new MenuItem();
        side.setId(UUID.randomUUID());
        order.addItem(side);

        order.setName("Max Mustermann");
        delivery = new Delivery(order);
    }

    @Test
    void shouldCreateDelivery() throws Exception {
        doNothing().when(deliveryRepository).addDelivery(any(Delivery.class));

        // place an order
        mockMvc.perform(MockMvcRequestBuilders.post("/api/delivery/delivery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Delivery created successfully"));

        // verify and get the saved delivery
        ArgumentCaptor<Delivery> deliveryCaptor = ArgumentCaptor.forClass(Delivery.class);
        verify(deliveryRepository, times(1)).addDelivery(deliveryCaptor.capture());
        Delivery savedDelivery = deliveryCaptor.getValue();

        // check the saved delivery is the same as given
        assertThat(savedDelivery.getOrder().getId()).isEqualTo(order.getId());
        assertThat(savedDelivery.getOrder().getName()).isEqualTo(order.getName());
        assertThat(savedDelivery.getOrder().getItems().stream().anyMatch(item -> item.getId().equals(drink.getId())))
                .isTrue();
        assertThat(savedDelivery.getOrder().getItems().stream().anyMatch(item -> item.getId().equals(meal.getId())))
                .isTrue();
        assertThat(savedDelivery.getOrder().getItems().stream().anyMatch(item -> item.getId().equals(side.getId())))
                .isTrue();
    }

    @Test
    void shouldRetrieveDeliveryById() throws Exception {
        when(deliveryRepository.getDeliveryByOrderId(eq(orderId))).thenReturn(Optional.of(delivery));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery/delivery/" + orderId))
                .andExpect(status().isOk())
                .andReturn();

        // Extract the JSON response as a Delivery object
        String jsonResponse = result.getResponse().getContentAsString();
        Delivery actualDelivery = new ObjectMapper().readValue(jsonResponse, Delivery.class);

        // assert retrieved delivery is correct
        assertThat(actualDelivery).isNotNull();
        assertThat(actualDelivery.getStatus()).isEqualTo(delivery.getStatus());
        assertThat(actualDelivery.toString()).isEqualTo(delivery.toString());
    }

    @Test
    void shouldReturnBadRequestForInvalidId() throws Exception {
        when(deliveryRepository.getDeliveryByOrderId(eq(orderId))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery/delivery/" + orderId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRetrieveAllDeliveries() throws Exception {
        when(deliveryRepository.getDeliveries()).thenReturn(List.of(delivery));

        MvcResult result =mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery/delivery"))
                .andExpect(status().isOk())
                .andReturn();

        // Extract the JSON response as a Delivery object
        String jsonResponse = result.getResponse().getContentAsString();
        Delivery[] actualDeliveries = new ObjectMapper().readValue(jsonResponse, Delivery[].class);

        // assert the list contains the correct delivery object
        assertThat(actualDeliveries).isNotNull();
        assertThat(actualDeliveries.length).isEqualTo(1);
        assertThat(actualDeliveries[0].toString()).isEqualTo(delivery.toString());
    }

    @Test
    void shouldStartDelivery() throws Exception {
        when(deliveryRepository.getDeliveryByOrderId(eq(orderId))).thenReturn(Optional.of(delivery));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/delivery/delivery/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"" + orderId + "\""))
                .andExpect(status().isOk())
                .andExpect(content().string("Started Delivery"));

        assertThat(delivery.getStatus()).isEqualTo("In Delivery");
    }

    @Test
    void shouldReturnBadRequestIfOrderNotFound() throws Exception {
        when(deliveryRepository.getDeliveryByOrderId(eq(orderId))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/delivery/delivery/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"" + orderId + "\""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("order not found"));
    }
}
