package onlineshop.shop.repository;

import onlineshop.shop.model.User;
import onlineshop.shop.model.enums.Role;
import onlineshop.shop.model.enums.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.UUID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void checkedIfUserExistsEmail() {
        User user =  User
                .builder()
                .id(1L)
                .email("User@mail.com")
                .number("11111111")
                .lastname("User")
                .name("User")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .build();
        userRepository.save(user);

        User checkUser = userRepository.findByEmail(user.getEmail());
        checkUser.setOrders(null);
        assertThat(checkUser).isEqualTo(user);
    }

    @Test
    void findByActivationCode() {
        User user =  User
                .builder()
                .id(1L)
                .email("User@mail.com")
                .number("11111111")
                .lastname("User")
                .name("User")
                .role(Role.USER)
                .status(Status.ACTIVE)
                .activationCode(UUID.randomUUID().toString())
                .build();
        userRepository.save(user);
        User checkUser = userRepository.findByActivationCode(user.getActivationCode());
        checkUser.setOrders(null);
        assertThat(checkUser).isEqualTo(user);
    }
}
