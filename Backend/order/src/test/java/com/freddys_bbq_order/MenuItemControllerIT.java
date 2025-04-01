package com.freddys_bbq_order;

import com.freddys_bbq_order.model.MenuItem;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MenuItemControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }

    @Order(1)
    @Test
    void shouldContainAllInitialMenuItems() throws Exception {
        // get the menu
        MvcResult result = mockMvc.perform(get("/api/order/menu"))
                .andExpect(status().isOk())
                .andReturn();

        // convert to list
        List<MenuItem> menuItems = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );

        // assert all menu items loaded correctly
        assertThat(menuItems).hasSize(7);
        assertThat(menuItems).extracting(MenuItem::getName)
                .containsExactlyInAnyOrder(
                        "Freddy's Rib Special",
                        "BBQ Burger and Fries",
                        "Mac and Cheese",
                        "Coleslaw Salad",
                        "Sweet Potatoe Mash",
                        "Lemonade",
                        "Beer"
                );
    }

    @Order(2)
    @Test
    void shouldCreateMenuItem() throws Exception {
        MenuItem menuItem = new MenuItem();
        menuItem.setName("menuItemDrink");
        menuItem.setCategory("Drink");
        menuItem.setPrice(2.99);
        menuItem.setImage("menuItemDrinkImage");

        // assert this menuitem is not in the menu at the beginning
        MvcResult initialResult = mockMvc.perform(get("/api/order/menu"))
                .andExpect(status().isOk())
                .andReturn();

        String initialResponse = initialResult.getResponse().getContentAsString();
        List<MenuItem> initialItems = objectMapper.readValue(initialResponse, new TypeReference<>() {});
        assertThat(initialItems).extracting(MenuItem::getName).doesNotContain("menuItemDrink");

        // add the menu item
        mockMvc.perform(post("/api/order/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(menuItem.getName()))
                .andExpect(jsonPath("$.category").value(menuItem.getCategory()))
                .andExpect(jsonPath("$.price").value(menuItem.getPrice()))
                .andExpect(jsonPath("$.image").value(menuItem.getImage()));

        // Get the current menu
        MvcResult finalResult = mockMvc.perform(get("/api/order/menu"))
                .andExpect(status().isOk())
                .andReturn();

        String finalResponse = finalResult.getResponse().getContentAsString();
        List<MenuItem> finalItems = objectMapper.readValue(finalResponse, new TypeReference<>() {});

        // assert the menuItem is now in the response list
        assertThat(finalItems).extracting(MenuItem::getName).contains(menuItem.getName());
        assertThat(finalItems).extracting(MenuItem::getCategory).contains(menuItem.getCategory());
        assertThat(finalItems).extracting(MenuItem::getPrice).contains(menuItem.getPrice());
        assertThat(finalItems).extracting(MenuItem::getImage).contains(menuItem.getImage());
    }


    @Test
    void shouldRetrieveMenuItemsByCategoryMainCourse() throws Exception {
        MenuItem menuItemMeal = new MenuItem();
        menuItemMeal.setName("menuItemMeal");
        menuItemMeal.setCategory("Main Course");
        menuItemMeal.setPrice(7.99);
        menuItemMeal.setImage("menuItemMealImage");

        mockMvc.perform(post("/api/order/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItemMeal)))
                .andExpect(status().isOk());

        // Get all main courses
        MvcResult finalResult = mockMvc.perform(get("/api/order/menu?category=Main Course"))
                .andExpect(status().isOk())
                .andReturn();

        String finalResponse = finalResult.getResponse().getContentAsString();
        List<MenuItem> finalItems = objectMapper.readValue(finalResponse, new TypeReference<>() {});

        // assert the menuItem is in the response list
        assertThat(finalItems).extracting(MenuItem::getName).contains(menuItemMeal.getName());
        assertThat(finalItems).extracting(MenuItem::getCategory).contains(menuItemMeal.getCategory());
        assertThat(finalItems).extracting(MenuItem::getPrice).contains(menuItemMeal.getPrice());
        assertThat(finalItems).extracting(MenuItem::getImage).contains(menuItemMeal.getImage());
        assertThat(finalItems).extracting(MenuItem::getCategory).doesNotContain("Side");
        assertThat(finalItems).extracting(MenuItem::getCategory).doesNotContain("Drink");

    }

    @Test
    void shouldRetrieveMenuItemsByCategorySide() throws Exception {
        MenuItem menuItemSide = new MenuItem();
        menuItemSide.setName("menuItemSide");
        menuItemSide.setCategory("Side");
        menuItemSide.setPrice(7.99);
        menuItemSide.setImage("menuItemSideImage");

        mockMvc.perform(post("/api/order/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItemSide)))
                .andExpect(status().isOk());

        // Get all Sides
        MvcResult finalResult = mockMvc.perform(get("/api/order/menu?category=Side"))
                .andExpect(status().isOk())
                .andReturn();

        String finalResponse = finalResult.getResponse().getContentAsString();
        List<MenuItem> finalItems = objectMapper.readValue(finalResponse, new TypeReference<>() {});

        // assert the menuItem is in the response list
        assertThat(finalItems).extracting(MenuItem::getName).contains(menuItemSide.getName());
        assertThat(finalItems).extracting(MenuItem::getCategory).contains(menuItemSide.getCategory());
        assertThat(finalItems).extracting(MenuItem::getPrice).contains(menuItemSide.getPrice());
        assertThat(finalItems).extracting(MenuItem::getImage).contains(menuItemSide.getImage());
        assertThat(finalItems).extracting(MenuItem::getCategory).doesNotContain("Main Course");
        assertThat(finalItems).extracting(MenuItem::getCategory).doesNotContain("Drink");

    }

    @Test
    void shouldRetrieveMenuItemsByCategoryDrink() throws Exception {

        MenuItem menuItemDrink = new MenuItem();
        menuItemDrink.setName("menuItemDrink");
        menuItemDrink.setCategory("Drink");
        menuItemDrink.setPrice(7.99);
        menuItemDrink.setImage("menuItemDrinkImage");

        mockMvc.perform(post("/api/order/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItemDrink)))
                .andExpect(status().isOk());

        // Get all Drinks
        MvcResult finalResult = mockMvc.perform(get("/api/order/menu?category=Drink"))
                .andExpect(status().isOk())
                .andReturn();

        String finalResponse = finalResult.getResponse().getContentAsString();
        List<MenuItem> finalItems = objectMapper.readValue(finalResponse, new TypeReference<>() {});

        // assert the menuItem is in the response list
        assertThat(finalItems).extracting(MenuItem::getName).contains(menuItemDrink.getName());
        assertThat(finalItems).extracting(MenuItem::getCategory).contains(menuItemDrink.getCategory());
        assertThat(finalItems).extracting(MenuItem::getPrice).contains(menuItemDrink.getPrice());
        assertThat(finalItems).extracting(MenuItem::getImage).contains(menuItemDrink.getImage());
        assertThat(finalItems).extracting(MenuItem::getCategory).doesNotContain("Side");
        assertThat(finalItems).extracting(MenuItem::getCategory).doesNotContain("Main Course");

    }

    @Test
    void shouldRetrieveMenuItemsById() throws Exception {

        MenuItem menuItemDrink = new MenuItem();
        menuItemDrink.setName("menuItemDrink");
        menuItemDrink.setCategory("Drink");
        menuItemDrink.setPrice(7.99);
        menuItemDrink.setImage("menuItemDrinkImage");

        MvcResult postResult = mockMvc.perform(post("/api/order/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItemDrink)))
                .andExpect(status().isOk())
                .andReturn();

        String postResponse = postResult.getResponse().getContentAsString();
        MenuItem postedItem = objectMapper.readValue(postResponse, new TypeReference<>() {});

        MvcResult getResult = mockMvc.perform(get("/api/order/menu?id="+postedItem.getId()))
                .andExpect(status().isOk())
                .andReturn();
        String getResponse = getResult.getResponse().getContentAsString();
        MenuItem getItem = objectMapper.readValue(getResponse, new TypeReference<>() {});

        assertThat(getItem.getName()).isEqualTo(postedItem.getName());
        assertThat(getItem.getCategory()).isEqualTo(postedItem.getCategory());
        assertThat(getItem.getImage()).isEqualTo(postedItem.getImage());
        assertThat(getItem.getPrice()).isEqualTo(postedItem.getPrice());
        assertThat(getItem.getId()).isEqualTo(postedItem.getId());
    }

    @Test
    void shouldReturnNotFoundBadId() throws Exception {
        mockMvc.perform(get("/api/order/menu?id="+UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestIdAndCategory() throws Exception {
        mockMvc.perform(get("/api/order/menu?id="+ UUID.randomUUID()+"&category=Drink"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldValidateItemIdViaApi() throws Exception {
        MenuItem menuItem = new MenuItem();
        menuItem.setName("Test Item");
        menuItem.setCategory("Side");
        menuItem.setPrice(5.50);
        menuItem.setImage("test-image.jpg");

        MvcResult postResult = mockMvc.perform(post("/api/order/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItem)))
                .andExpect(status().isOk())
                .andReturn();

        String postResponse = postResult.getResponse().getContentAsString();
        MenuItem postedItem = objectMapper.readValue(postResponse, new TypeReference<>() {});

        MvcResult validateResult = mockMvc.perform(get("/api/order/menu/validate-id?id="+postedItem.getId()))
                .andExpect(status().isOk())
                .andReturn();

        String validateResponse = validateResult.getResponse().getContentAsString();
        Boolean isValid = objectMapper.readValue(validateResponse, Boolean.class);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnFalseForNonExistingItemId() throws Exception {
        UUID randomId = UUID.randomUUID();

        MvcResult result = mockMvc.perform(get("/api/order/menu/validate-id")
                        .param("id", randomId.toString()))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Boolean isValid = objectMapper.readValue(response, Boolean.class);

        assertThat(isValid).isFalse();
    }

    @Test
    void shouldReturn400ForInvalidUUID() throws Exception {
        mockMvc.perform(get("/api/order/menu/validate-id?id=not-a-valid-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid UUID format")))
                .andReturn();
    }
}
