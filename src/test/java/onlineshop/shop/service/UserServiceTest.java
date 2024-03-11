package onlineshop.shop.service;

import onlineshop.shop.model.User;
import onlineshop.shop.model.enums.Status;
import onlineshop.shop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsersFromRepository() {
        User user1 = User.builder()
                .id(1L)
                .email("User1@mail.com")
                .build();
        User user2 = User.builder()
                .id(2L)
                .email("User2@mail.com")
                .build();
        List<User> allUsers = List.of(user1,user2);

        when(userRepository.findAll()).thenReturn(allUsers);

        List<User> result = userService.allUsers();
        assertThat(result).isEqualTo(allUsers);
        verify(userRepository).findAll();
    }

    @Test
    void userForIdFromRepository() {
        User user = User.builder()
                .id(1L)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        User result = userService.userForId(1L);
        assertThat(result).isEqualTo(user);
        verify(userRepository).findById(1L);
    }

    @Test
    void userForPrincipalFromRepository() {
        Principal principal = mock(Principal.class);
        User user = User.builder()
                .id(1L)
                .email("User@mail.com")
                .build();
        when(principal.getName()).thenReturn("User@mail.com");
        when(userRepository.findByEmail("User@mail.com")).thenReturn(user);

        User result = userService.userForPrincipal(principal);
        assertThat(result).isEqualTo(user);
        verify(principal).getName();
        verify(userRepository).findByEmail("User@mail.com");
    }

    @Test
    void updateUser() {
        User user = User.builder()
                .id(1L)
                .email("User@email.com")
                .name("User")
                .lastname("User")
                .number("User")
                .password("User")
                .build();
        User newUser = User.builder()
                .email("new@email.com")
                .name("New Name")
                .lastname("New Lastname")
                .number("1234567890")
                .password("newPassword")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("newPassword");

        userService.updateUser(1L, newUser);

        verify(userRepository).findById(1L);

        assertThat(user.getEmail()).isEqualTo("new@email.com");
        assertThat(user.getName()).isEqualTo("New Name");
        assertThat(user.getLastname()).isEqualTo("New Lastname");
        assertThat(user.getNumber()).isEqualTo("1234567890");
        assertThat(user.getPassword()).isEqualTo("newPassword");


        verify(userRepository).save(user);
    }

    @Test
    void banActiveUser() {
        User user = User.builder()
                .id(1L)
                .email("User@mail.com")
                .status(Status.ACTIVE)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        userService.banUser(1L);

        assertThat(Objects.requireNonNull(user).getStatus()).isEqualTo(Status.BANNED);
    }
    @Test
    void unbanActiveUser() {
        User user = User.builder()
                .id(1L)
                .email("User@mail.com")
                .status(Status.BANNED)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));

        userService.banUser(1L);

        assertThat(Objects.requireNonNull(user).getStatus()).isEqualTo(Status.ACTIVE);
    }
}