package onlineshop.shop.controller;

import onlineshop.shop.model.Cart;
import onlineshop.shop.repository.CartRepository;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/sql_insert.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CartControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private CartRepository cartRepository;

    @Test
    @WithMockUser(username = USER_EMAIL1, password = USER_PASSWORD)
    void checkPageGetCart() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(USER_EMAIL1);

        mockMvc.perform(formLogin().user(USER_EMAIL1).password(USER_PASSWORD))
                .andExpect(authenticated());
        mockMvc.perform(get("/cart")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"));

        Cart cart = cartRepository.findById(ID).orElseThrow();
        assertThat(cart.getFinalPrice()).isEqualTo(ORDER_TOTAL_PRICE);
    }

    @Test
    @WithMockUser(username = USER_EMAIL1, password = USER_PASSWORD)
    void checkAddNewProductToCart() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(USER_EMAIL1);

        mockMvc.perform(formLogin().user(USER_EMAIL1).password(USER_PASSWORD))
                .andExpect(authenticated());
        mockMvc.perform(post("/cart/add")
                        .param("product_id", "3")
                        .principal(principal)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

        Cart cart = cartRepository.findById(ID).orElseThrow();
        assertThat(cart.getFinalPrice()).isEqualTo(ORDER_TOTAL_PRICE + 1000.0);
    }

    @Test
    @Transactional
    @WithMockUser(username = USER_EMAIL1, password = USER_PASSWORD)
    void checkClearCartMethod() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(USER_EMAIL1);

        mockMvc.perform(formLogin().user(USER_EMAIL1).password(USER_PASSWORD))
                .andExpect(authenticated());
        mockMvc.perform(delete("/cart/clear")
                        .principal(principal)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

        Cart cart = cartRepository.findById(ID).orElseThrow();
        assertThat(cart.getFinalPrice()).isEqualTo(0);
        assertThat(cart.getCartList().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @WithMockUser(username = USER_EMAIL1, password = USER_PASSWORD)
    void deleteCartItem() throws Exception {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(USER_EMAIL1);

        mockMvc.perform(formLogin().user(USER_EMAIL1).password(USER_PASSWORD))
                .andExpect(authenticated());
        mockMvc.perform(delete("/cart/deleteItem")
                        .param("cartItem_Id", "1")
                        .principal(principal)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

        Cart cart = cartRepository.findById(ID).orElseThrow();
        assertThat(cart.getFinalPrice()).isEqualTo(ORDER_TOTAL_PRICE-300);
    }

    @Test
    void editQuantity() {
    }
}