package com.freddys_bbq_frontend;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.not;

import com.freddys_bbq_frontend.model.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@WebMvcTest(MenuController.class)
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuItemRepository menuItemRepository;

    private final List<MenuItem> menuItems = Arrays.asList(
            new MenuItem(UUID.fromString("00000000-0000-0000-0000-000000000001"), "Freddy's Ribs Special", 20, false),
            new MenuItem(UUID.fromString("00000000-0000-0000-0000-000000000002"), "Freddy's Lemonade", 5, true),
            new MenuItem(UUID.fromString("00000000-0000-0000-0000-000000000003"), "Coleslaw Salad", 3, false)
            );

    @BeforeEach
    void setUp() {
        when(menuItemRepository.findByOrderByDrinkDescNameDesc()).thenReturn(this.menuItems);
    }

    @Test
    void testIndexEndpoint() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())  // HTTP 200 OK
                .andExpect(view().name("index"))  // View-Name ist "index"
                .andExpect(model().attributeExists("menuItems"))  // "menuItems" ist im Model vorhanden
                .andExpect(model().attribute("menuItems", this.menuItems));  // Vergleicht den Inhalt
    }


    @Test
    void testIndexEndpointFalse() throws Exception {
        List<MenuItem> copyMenu = new java.util.ArrayList<>(this.menuItems);
        copyMenu.remove(0);
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())  // HTTP 200 OK
                .andExpect(view().name("index"))  // View-Name ist "index"
                .andExpect(model().attributeExists("menuItems"))  // "menuItems" ist im Model vorhanden
                .andExpect(model().attribute("menuItems", not(copyMenu)));  // Vergleicht den Inhalt
    }
}
