package onlineshop.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import onlineshop.shop.model.User;
import onlineshop.shop.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static onlineshop.shop.util.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private UserService userService;


    @Test
    void getLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void getRegistrationPage() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    void createUser() throws Exception {
        User user = User.builder()
                .name(USER_NAME)
                .lastname(USER_LASTNAME)
                .email(USER_EMAIL)
                .number(USER_NUMBER)
                .password(USER_PASSWORD)
                .build();

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk());
    }


    @Test
    void activateUser() {

    }

    @Test
    void getAccountPage() throws Exception {
        mockMvc.perform(get("/user/getAccount"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/menu"));
    }

    @Test
    void changingUserData() {
    }

    @Test
    void testChangingUserData() {
    }
}