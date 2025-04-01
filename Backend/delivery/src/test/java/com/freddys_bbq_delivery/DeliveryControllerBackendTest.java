package com.freddys_bbq_delivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freddys_bbq_delivery.model.DeliveryD;
import com.freddys_bbq_delivery.model.MenuItemD;
import com.freddys_bbq_delivery.model.OrderD;
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

import java.lang.reflect.Field;
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
    private DeliveryD delivery;
    private OrderD order;
    private MenuItemD drink;
    private MenuItemD side;
    private MenuItemD meal;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        mockMvc = MockMvcBuilders.standaloneSetup(deliveryControllerBackend).build();

        order = new OrderD();
        orderId = UUID.randomUUID();
        order.setId(orderId);

        drink = new MenuItemD();
        drink.setId(UUID.randomUUID());
        order.addItem(drink);

        meal = new MenuItemD();
        meal.setId(UUID.randomUUID());
        order.addItem(meal);

        side = new MenuItemD();
        side.setId(UUID.randomUUID());
        order.addItem(side);

        order.setName("Max Mustermann");
        delivery = new DeliveryD(order);
        Field idField = DeliveryD.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(delivery, UUID.randomUUID());
    }

    @Test
    void shouldCreateDelivery() throws Exception {
        when(deliveryRepository.save(any(DeliveryD.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // place an order
        mockMvc.perform(MockMvcRequestBuilders.post("/api/delivery/delivery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Delivery created successfully"));

        // verify and get the saved delivery
        ArgumentCaptor<DeliveryD> deliveryCaptor = ArgumentCaptor.forClass(DeliveryD.class);
        verify(deliveryRepository, times(1)).save(deliveryCaptor.capture());
        DeliveryD savedDelivery = deliveryCaptor.getValue();

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
        when(deliveryRepository.findByOrderId(eq(orderId))).thenReturn(Optional.of(delivery));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery/delivery/" + orderId))
                .andExpect(status().isOk())
                .andReturn();

        // Extract the JSON response as a Delivery object
        String jsonResponse = result.getResponse().getContentAsString();
        DeliveryD actualDelivery = new ObjectMapper().readValue(jsonResponse, DeliveryD.class);

        // assert retrieved delivery is correct
        assertThat(actualDelivery).isNotNull();
        assertThat(actualDelivery.getStatus()).isEqualTo(delivery.getStatus());
        assertThat(actualDelivery.toString()).isEqualTo(delivery.toString());
    }

    @Test
    void shouldReturnBadRequestForInvalidId() throws Exception {
        when(deliveryRepository.findByOrderId(eq(orderId))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery/delivery/" + orderId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRetrieveAllDeliveries() throws Exception {
        when(deliveryRepository.findAll()).thenReturn(List.of(delivery));

        MvcResult result =mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery/delivery"))
                .andExpect(status().isOk())
                .andReturn();

        // Extract the JSON response as a Delivery object
        String jsonResponse = result.getResponse().getContentAsString();
        DeliveryD[] actualDeliveries = new ObjectMapper().readValue(jsonResponse, DeliveryD[].class);

        // assert the list contains the correct delivery object
        assertThat(actualDeliveries).isNotNull();
        assertThat(actualDeliveries.length).isEqualTo(1);
        assertThat(actualDeliveries[0].toString()).isEqualTo(delivery.toString());
    }

    @Test
    void shouldStartDelivery() throws Exception {
        when(deliveryRepository.findById(eq(delivery.getId()))).thenReturn(Optional.of(delivery));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/delivery/delivery/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"" + delivery.getId() + "\""))
                .andExpect(status().isOk())
                .andExpect(content().string("Started Delivery"));

        assertThat(delivery.getStatus()).isEqualTo("In Delivery");
    }

    @Test
    void shouldMarkDeliveryAsDelivered() throws Exception {
        when(deliveryRepository.findById(eq(delivery.getId()))).thenReturn(Optional.of(delivery));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/delivery/delivery/delivered")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"" + delivery.getId() + "\""))
                .andExpect(status().isOk())
                .andExpect(content().string("Delivery marked as Delivered"));

        assertThat(delivery.getStatus()).isEqualTo("Delivered");
    }

    @Test
    void shouldReturnBadRequestIfOrderNotFoundAtDelivered() throws Exception {
        when(deliveryRepository.findById(eq(delivery.getId()))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/delivery/delivery/delivered")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"" + delivery.getId() + "\""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("order not found"));
    }

    @Test
    void shouldReturnBadRequestIfOrderNotFoundAtStart() throws Exception {
        when(deliveryRepository.findById(eq(delivery.getId()))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/delivery/delivery/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"" + delivery.getId() + "\""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Delivery not found"));
    }
}
