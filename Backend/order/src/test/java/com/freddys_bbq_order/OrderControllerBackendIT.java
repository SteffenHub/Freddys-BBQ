package com.freddys_bbq_order;

import com.freddys_bbq_order.model.MenuItemO;
import com.freddys_bbq_order.model.OrderO;
import com.freddys_bbq_order.model.OrderRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
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
@TestPropertySource(properties = {
        "DELIVERY_BACKEND_URL=http://localhost:8081",
        "rabbitmq.exchange.name=",
        "rabbitmq.routing.key="
})
class OrderControllerBackendIT {

    @MockitoBean
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MenuItemO mainCourseMenuItem;

    private MenuItemO sideMenuItem;

    private MenuItemO drinkMenuItem;

    @Value("${DELIVERY_BACKEND_URL:http://localhost:8081}")
    private String deliveryBackendUrl;

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

    @BeforeEach
    void setUp() throws Exception {
        mainCourseMenuItem = new MenuItemO();
        mainCourseMenuItem.setName("mainCourseMenuItem");
        mainCourseMenuItem.setCategory("Main Course");
        mainCourseMenuItem.setPrice(1.0);
        mainCourseMenuItem.setImage("mainCourseImage");
        // add the menu item
        MvcResult result = mockMvc.perform(post("/api/order/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mainCourseMenuItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(mainCourseMenuItem.getName()))
                .andExpect(jsonPath("$.category").value(mainCourseMenuItem.getCategory()))
                .andExpect(jsonPath("$.price").value(mainCourseMenuItem.getPrice()))
                .andExpect(jsonPath("$.image").value(mainCourseMenuItem.getImage())).andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        MenuItemO createdMenuItem = objectMapper.readValue(jsonResponse, MenuItemO.class);
        mainCourseMenuItem.setId(createdMenuItem.getId());

        sideMenuItem = new MenuItemO();
        sideMenuItem.setName("sideMenuItem");
        sideMenuItem.setCategory("Side");
        sideMenuItem.setPrice(1.0);
        sideMenuItem.setImage("sideImage");
        // add the menu item
        result = mockMvc.perform(post("/api/order/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sideMenuItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(sideMenuItem.getName()))
                .andExpect(jsonPath("$.category").value(sideMenuItem.getCategory()))
                .andExpect(jsonPath("$.price").value(sideMenuItem.getPrice()))
                .andExpect(jsonPath("$.image").value(sideMenuItem.getImage())).andReturn();
        jsonResponse = result.getResponse().getContentAsString();
        createdMenuItem = objectMapper.readValue(jsonResponse, MenuItemO.class);
        sideMenuItem.setId(createdMenuItem.getId());

        drinkMenuItem = new MenuItemO();
        drinkMenuItem.setName("drinkMenuItem");
        drinkMenuItem.setCategory("Drink");
        drinkMenuItem.setPrice(1.0);
        drinkMenuItem.setImage("drinkImage");
        // add the menu item
        result = mockMvc.perform(post("/api/order/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(drinkMenuItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(drinkMenuItem.getName()))
                .andExpect(jsonPath("$.category").value(drinkMenuItem.getCategory()))
                .andExpect(jsonPath("$.price").value(drinkMenuItem.getPrice()))
                .andExpect(jsonPath("$.image").value(drinkMenuItem.getImage())).andReturn();
        jsonResponse = result.getResponse().getContentAsString();
        createdMenuItem = objectMapper.readValue(jsonResponse, MenuItemO.class);
        drinkMenuItem.setId(createdMenuItem.getId());
    }

    /**
     * Test findAll() GET api/order/orders
     * @throws Exception
     */
    @Test
    @Order(1)
    void shouldContainAllNoOrderAtStart() throws Exception {
        // get the menu
        MvcResult result = mockMvc.perform(get("/api/order/orders"))
                .andExpect(status().isOk())
                .andReturn();

        // convert to list
        List<OrderO> menuItems = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );

        // assert all menu items loaded correctly
        assertThat(menuItems).hasSize(0);
    }

    /**
     * Test placeOrder() POST /api/order/orders
     *
     * @throws Exception
     */
    @Test
    void shouldCreateOrder() throws Exception {
        // set up mockito
        Mockito.when(restTemplate.postForEntity(
                        eq("http://localhost:8081/api/delivery/delivery"),
                        any(OrderO.class),
                        eq(String.class)))
                .thenReturn(new ResponseEntity<>("Delivery Created", HttpStatus.OK));

        // create new Order
        OrderRequest orderRequest = new OrderRequest("Max Mustermann", "mail@example.com", new ArrayList<>(){{add(drinkMenuItem.getId()); add(mainCourseMenuItem.getId()); add(sideMenuItem.getId());}});
        MvcResult result = mockMvc.perform(post("/api/order/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated()).andReturn();
        UUID responseUUID = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        assertThat(responseUUID).isNotNull();

        // verify call from restTemplate
        Mockito.verify(restTemplate, times(1))
                .postForEntity(eq("http://localhost:8081/api/delivery/delivery"), any(OrderO.class), eq(String.class));

        // confirm the order can be found
        result = mockMvc.perform(get("/api/order/orders"))
                .andExpect(status().isOk())
                .andReturn();
        List<OrderO> orders = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        assertThat(orders.stream().anyMatch(order -> order.getId().equals(responseUUID)))
                .isTrue();
        assertThat(orders.stream().anyMatch(order -> order.getName().equals("Max Mustermann")))
                .isTrue();
        assertThat(orders.stream().anyMatch(order -> order.getItems().stream().anyMatch(item -> item.getId().equals(drinkMenuItem.getId()))))
                .isTrue();
        assertThat(orders.stream().anyMatch(order -> order.getItems().stream().anyMatch(item -> item.getId().equals(mainCourseMenuItem.getId()))))
                .isTrue();
        assertThat(orders.stream().anyMatch(order -> order.getItems().stream().anyMatch(item -> item.getId().equals(sideMenuItem.getId()))))
                .isTrue();
    }

    /**
     * Test placeOrder() POST /api/order/orders
     * invalid menuItem id given
     * @throws Exception
     */
    @Test
    void shouldNotAcceptInvalidId() throws Exception {
        // create new Order wrong drink id
        OrderRequest orderRequest = new OrderRequest("Max Mustermann", "mail@example.com", new ArrayList<>(){{add(UUID.randomUUID());}});
        mockMvc.perform(post("/api/order/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test placeOrder() POST /api/order/orders
     * bad request from delivery server
     * @throws Exception
     */
    @Test
    void shouldForwardBadRequest() throws Exception {
        // set up mockito
        Mockito.when(restTemplate.postForEntity(
                        eq("http://localhost:8081/api/delivery/delivery"),
                        any(OrderO.class),
                        eq(String.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

        // create new Order
        OrderRequest orderRequest = new OrderRequest("Max Mustermann","mail@example.com", new ArrayList<>(){{add(drinkMenuItem.getId()); add(mainCourseMenuItem.getId()); add(sideMenuItem.getId());}});
        mockMvc.perform(post("/api/order/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test placeOrder() POST /api/order/orders
     * Delivery service not reachable
     * @throws Exception
     */
    @Test
    void testDeliveryServiceNotReachable() throws Exception {
        OrderRequest orderRequest = new OrderRequest("Max Mustermann","mail@example.com", new ArrayList<>(){{add(drinkMenuItem.getId()); add(mainCourseMenuItem.getId()); add(sideMenuItem.getId());}});
        mockMvc.perform(post("/api/order/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isBadRequest());
    }
}
