package onlineshop.shop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

import static onlineshop.shop.util.TestConstants.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/order.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;


    @Test
    @Transactional
    @WithMockUser(username = USER_EMAIL1, password = USER_PASSWORD)
    void getPageCheckDetailsOfOrder() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(USER_EMAIL1);

        mockMvc.perform(formLogin().user(USER_EMAIL1).password(USER_PASSWORD))
                .andExpect(authenticated());
        mockMvc.perform(get("/order")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("cart"))
                .andExpect(model().attribute("order",hasProperty("totalPrice",is(ORDER_TOTAL_PRICE))));
    }

    @Test
    @Transactional
    @WithMockUser(username = USER_EMAIL1, password = USER_PASSWORD)
    void testOrder() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(USER_EMAIL1);

        mockMvc.perform(formLogin().user(USER_EMAIL1).password(USER_PASSWORD))
                .andExpect(authenticated());
        mockMvc.perform(post("/order")
                        .with(csrf())
                        .principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/order/orderSuccess/" + 1));
    }

    @Test
    @Transactional
    @WithMockUser(username = USER_EMAIL1, password = USER_PASSWORD)
    void checkIfOrderSuccessGetPageOrderSuccess() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(USER_EMAIL1);

        mockMvc.perform(formLogin().user(USER_EMAIL1).password(USER_PASSWORD))
                .andExpect(authenticated());
        mockMvc.perform(get("/order/orderSuccess/" + 1)
                        .principal(principal)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("orderSuccess"))
                .andExpect(model().attribute("order", hasProperty("totalPrice", is(ORDER_TOTAL_PRICE))))
                .andExpect(model().attribute("order", hasProperty("date", is(ORDER_DATE_TIME))));
    }

    @Test
    @WithMockUser(username = USER_EMAIL1, password = USER_PASSWORD)
    void checkGetPageMyOrders() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(USER_EMAIL1);

        mockMvc.perform(formLogin().user(USER_EMAIL1).password(USER_PASSWORD))
                .andExpect(authenticated());
        mockMvc.perform(get("/order/myOrders")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("/account/myOrders"));
    }
}