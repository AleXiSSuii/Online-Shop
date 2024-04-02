package onlineshop.shop.controller;

import onlineshop.shop.model.User;
import onlineshop.shop.model.enums.Role;
import onlineshop.shop.model.enums.Status;
import onlineshop.shop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static onlineshop.shop.util.TestConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User user1 = User.builder()
                .name(USER_NAME)
                .lastname(USER_LASTNAME)
                .email(USER_EMAIL1)
                .number(USER_NUMBER)
                .password(USER_BCRYPT_PASSWORD)
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        User user2 = User.builder()
                .name(USER_NAME)
                .lastname(USER_LASTNAME)
                .email(USER_EMAIL2)
                .number(USER_NUMBER)
                .password(USER_BCRYPT_PASSWORD)
                .role(Role.USER)
                .status(Status.NOTACTIVATE)
                .activationCode(ACTIVATION_CODE)
                .build();
        if(userRepository.findByEmail(USER_EMAIL1) == null || userRepository.findByEmail(USER_EMAIL2) == null){
            userRepository.save(user1);
            userRepository.save(user2);

        }
    }

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
    void createUserIfDataIsValid() throws Exception {
        User user = User.builder()
                .name(USER_NAME)
                .lastname(USER_LASTNAME)
                .email("asd@asdsaasasd.com")
                .number(USER_NUMBER)
                .password(USER_BCRYPT_PASSWORD)
                .build();


        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .flashAttr("user", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    @Transactional
    void createUserIfItAlreadyExist() throws Exception {
        User user = userRepository.findByEmail(USER_EMAIL1);

        mockMvc.perform(post("/registration")
                        .with(csrf())
                        .flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"));
    }

    @Test
    void activateUserWithActivationCode() throws Exception {
        mockMvc.perform(get("/activate/" + ACTIVATION_CODE))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login?activated=true"));

        User user = userRepository.findByEmail(USER_EMAIL2);
        assertThat(user.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    void getAccountPage() throws Exception {
        mockMvc.perform(get("/user/getAccount"))
                .andExpect(status().isFound());
    }

    @Test
    void getChangingUserDataPageUnauthorized() throws Exception {
        mockMvc.perform(get("/user/changingUserData"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = USER_EMAIL1, password = USER_PASSWORD)
    @Transactional
    void getChangingUserDataPage() throws Exception {
        mockMvc.perform(formLogin().user(USER_EMAIL1).password(USER_PASSWORD))
                .andExpect(authenticated());
        mockMvc.perform(get("/user/changingUserData"))
                .andExpect(status().isOk())
                .andExpect(view().name("/account/changingUserData")).andExpect(model()
                        .attribute("user", hasProperty("email", is("user@mail.com"))))
                .andExpect(model().attribute("user", hasProperty("name", is("User"))))
                .andExpect(model().attribute("user", hasProperty("lastname", is("User"))))
                .andExpect(model().attribute("user", hasProperty("number", is("12345678"))));
    }

    @Test
    void testChangingUserData() {

    }
}