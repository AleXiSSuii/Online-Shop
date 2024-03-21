package onlineshop.shop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.security.Principal;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void order() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("user");
        mockMvc.perform(get("/order").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("cart"));
    }

    @Test
    void testOrder() {
    }

    @Test
    void orderSuccess() {
    }

    @Test
    void myOrders() {
    }
}