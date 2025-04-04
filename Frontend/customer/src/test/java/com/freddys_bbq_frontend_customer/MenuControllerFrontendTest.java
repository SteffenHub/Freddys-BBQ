package com.freddys_bbq_frontend_customer;

import com.freddys_bbq_frontend_customer.model.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class MenuControllerFrontendTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MenuControllerFrontend menuController;

    @Mock
    private Model model;

    private final String orderBackendUrl = "http://localhost:8080";

    private MenuItem drinkItem;
    private MenuItem mealItem;
    private MenuItem sideItem;

    @BeforeEach
    void setUp() {
        menuController = new MenuControllerFrontend(restTemplate);
        ReflectionTestUtils.setField(menuController, "orderBackendUrl", "http://localhost:8080");
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
    void shouldLoadMenuItemsSuccessfully() {
        when(restTemplate.getForEntity(eq(orderBackendUrl + "/api/order/menu?category=Drink"), eq(MenuItem[].class)))
                .thenReturn(ResponseEntity.ok(new MenuItem[]{drinkItem}));
        when(restTemplate.getForEntity(eq(orderBackendUrl + "/api/order/menu?category=Main Course"), eq(MenuItem[].class)))
                .thenReturn(ResponseEntity.ok(new MenuItem[]{mealItem}));
        when(restTemplate.getForEntity(eq(orderBackendUrl + "/api/order/menu?category=Side"), eq(MenuItem[].class)))
                .thenReturn(ResponseEntity.ok(new MenuItem[]{sideItem}));

        String viewName = menuController.index(model);

        verify(model).addAttribute("drinks", Collections.singletonList(drinkItem));
        verify(model).addAttribute("meals", Collections.singletonList(mealItem));
        verify(model).addAttribute("sides", Collections.singletonList(sideItem));
        verify(model, never()).addAttribute(eq("errorMessage"), anyString());

        assertThat(viewName).isEqualTo("index");
    }

    @Test
    void shouldHandleBackendError() {
        when(restTemplate.getForEntity(orderBackendUrl + "/api/order/menu?category=Drink", MenuItem[].class))
                .thenThrow(new RestClientException("error"));

        String viewName = menuController.index(model);

        verify(model).addAttribute("errorMessage", "The menu cannot be loaded. Please try again later (The Backend does not answer)");
        verify(model).addAttribute("drinks", Collections.emptyList());
        verify(model).addAttribute("meals", Collections.emptyList());
        verify(model).addAttribute("sides", Collections.emptyList());

        assertThat(viewName).isEqualTo("index");
    }

    @Test
    void shouldValidateItemId() {
        UUID itemID = UUID.randomUUID();
        when(restTemplate.getForEntity(
                orderBackendUrl + "/api/order/menu/validate-id?id=" + itemID,
                Boolean.class)
        ).thenReturn(ResponseEntity.ok(true));

        ResponseEntity<Boolean> response = menuController.validateId(itemID);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isTrue();
    }

    @Test
    void shouldReturnFalseForInvalidItemId() {
        UUID itemID = UUID.randomUUID();
        when(restTemplate.getForEntity(orderBackendUrl + "/api/order/menu/validate-id?id=" + itemID, Boolean.class))
                .thenReturn(ResponseEntity.ok(false));

        ResponseEntity<Boolean> response = menuController.validateId(itemID);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isFalse();
    }

    @Test
    void shouldReturnInternalServerErrorWhenBackendFails() {
        UUID itemID = UUID.randomUUID();
        when(restTemplate.getForEntity(orderBackendUrl + "/api/order/menu/validate-id?id=" + itemID, Boolean.class))
                .thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

        ResponseEntity<Boolean> response = menuController.validateId(itemID);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void shouldHandleRestClientException() {
        UUID itemID = UUID.randomUUID();
        when(restTemplate.getForEntity(orderBackendUrl + "/api/order/menu/validate-id?id=" + itemID, Boolean.class))
                .thenThrow(new RestClientException("Connection error"));

        ResponseEntity<Boolean> response = menuController.validateId(itemID);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
