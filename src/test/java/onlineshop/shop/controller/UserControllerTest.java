package onlineshop.shop.controller;

import onlineshop.shop.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static onlineshop.shop.util.TestConstants.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;


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
                .with(csrf())
                .flashAttr("user", user))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    void activateUser() {

    }

    @Test
    void getAccountPage() throws Exception {
        mockMvc.perform(get("/user/getAccount"))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(username = "user@mail.com", roles = "USER")
    void getChangingUserDataPage() throws Exception {
        User user = User.builder()
                .name(USER_NAME)
                .lastname(USER_LASTNAME)
                .email(USER_EMAIL)
                .number(USER_NUMBER)
                .password(USER_PASSWORD)
                .build();
        mockMvc.perform(get("/user/changingUserData"))
                .andExpect(status().isFound())
                .andExpect(view().name("changingUserData"))
                .andExpect(model().attribute("user", hasProperty("email",is("user@mail.com"))))
                .andExpect(model().attribute("user", hasProperty("name",is("User"))))
                .andExpect(model().attribute("user", hasProperty("lastname",is("User"))))
                .andExpect(model().attribute("user", hasProperty("number",is("12345678"))))
                .andExpect(model().attribute("user", hasProperty("password",is("123"))));
    }

    @Test
    void testChangingUserData() {
    }
}