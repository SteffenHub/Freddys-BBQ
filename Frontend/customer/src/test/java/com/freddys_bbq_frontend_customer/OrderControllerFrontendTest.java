package com.freddys_bbq_frontend_customer;

import com.freddys_bbq_frontend_customer.model.MenuItem;
import com.freddys_bbq_frontend_customer.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerFrontendTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderControllerFrontend orderController;

    @Mock
    private Model model;

    private MenuItem drinkItem;
    private MenuItem mealItem;
    private MenuItem sideItem;
    private UUID orderId;

    @BeforeEach
    void setUp() {
        orderController = new OrderControllerFrontend(restTemplate);
        orderId = UUID.randomUUID();

        drinkItem = new MenuItem();
        drinkItem.setName("Cola");
        drinkItem.setCategory("Drink");
        drinkItem.setPrice(1.99);
        drinkItem.setImage("coke.jpg");

        mealItem = new MenuItem();
        mealItem.setName("Burger");
        mealItem.setCategory("Main Course");
        mealItem.setPrice(5.99);
        mealItem.setImage("burger.jpg");

        sideItem = new MenuItem();
        sideItem.setName("Fries");
        sideItem.setCategory("Side");
        sideItem.setPrice(2.99);
        sideItem.setImage("fries.jpg");
    }

    @Test
    void shouldRetrieveOrderSuccessfully() {
        Order order = new Order();
        order.setId(orderId);

        when(restTemplate.getForEntity(anyString(), eq(Order.class)))
                .thenReturn(ResponseEntity.ok(order));

        String viewName = orderController.getOrder(orderId, model);

        verify(model).addAttribute("order", order);
        verify(model, never()).addAttribute(eq("errorMessage"), anyString());

        assertThat(viewName).isEqualTo("order-info");
    }

    @Test
    void shouldHandleOrderNotFound() {
        when(restTemplate.getForEntity(anyString(), eq(Order.class)))
                .thenReturn(ResponseEntity.status(404).build());

        String viewName = orderController.getOrder(orderId, model);

        verify(model).addAttribute(eq("order"), any(Order.class));
        verify(model).addAttribute("errorMessage", "The order could not be found or the Backend does not answer");

        assertThat(viewName).isEqualTo("order-info");
    }

    @Test
    void shouldShowOrderFormSuccessfully() {
        when(restTemplate.getForEntity(anyString(), eq(MenuItem.class)))
                .thenReturn(ResponseEntity.ok(drinkItem))
                .thenReturn(ResponseEntity.ok(mealItem))
                .thenReturn(ResponseEntity.ok(sideItem));

        List<UUID> cart = new ArrayList<>(){{add(drinkItem.getId());add(mealItem.getId());add(sideItem.getId());}};
        String viewName = orderController.showOrderForm(cart.toString(), model);

        verify(model).addAttribute("cartItems", new ArrayList<>(){{add(drinkItem);add(mealItem);add(sideItem);}});
        verify(model, never()).addAttribute(eq("errorMessage"), anyString());

        assertThat(viewName).isEqualTo("order");
    }

    @Test
    void testEmptyCart() {
        String viewName = orderController.showOrderForm("[]", model);

        verify(model).addAttribute("cartItems", Collections.emptyList());
        verify(model).addAttribute("errorMessage", "The Cart is empty");

        assertThat(viewName).isEqualTo("order");
    }

    @Test
    void shouldHandleBackendErrorOnShowOrderForm() {
        when(restTemplate.getForEntity(anyString(), eq(MenuItem.class)))
                .thenThrow(new RestClientException("error"));

        String viewName = orderController.showOrderForm(new ArrayList<>(){{add(null);}}.toString(), model);

        verify(model).addAttribute("errorMessage", "The menu cannot be loaded. Please try again later (The Backend does not answer)");
        verify(model).addAttribute("cartItems", Collections.emptyList());

        assertThat(viewName).isEqualTo("order");
    }

    @Test
    void shouldHandleCartError() {
        String viewName = orderController.showOrderForm("[nothing here]", model);

        verify(model).addAttribute("errorMessage", "Error loading the cart");
        verify(model).addAttribute("cartItems", Collections.emptyList());

        assertThat(viewName).isEqualTo("order");
    }

    @Test
    void shouldPlaceOrderSuccessfully() {
        UUID orderResponseId = UUID.randomUUID();
        when(restTemplate.postForEntity(anyString(), any(), eq(UUID.class)))
                .thenReturn(ResponseEntity.ok(orderResponseId));

        String viewName = orderController.placeOrder("Max", "mail@example.com", new ArrayList<>(){{add(UUID.randomUUID()); add(UUID.randomUUID()); add(UUID.randomUUID());}}, model);

        assertThat(viewName).isEqualTo("redirect:/delivery/" + orderResponseId);
    }

    @Test
    void shouldHandleFailedOrderPlacement() {
        when(restTemplate.postForEntity(anyString(), any(), eq(UUID.class)))
                .thenThrow(new RestClientException("error"));

        String viewName = orderController.placeOrder("Max","mail@example.com", new ArrayList<>(){{add(UUID.randomUUID()); add(UUID.randomUUID()); add(UUID.randomUUID());}}, model);

        verify(model).addAttribute("errorMessage", "The Order cannot be placed");
        assertThat(viewName).isEqualTo("order");
    }
}
