package onlineshop.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onlineshop.shop.model.Cart;
import onlineshop.shop.model.User;
import onlineshop.shop.model.enums.Role;
import onlineshop.shop.model.enums.Status;
import onlineshop.shop.repository.CartRepository;
import onlineshop.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public boolean createUser(User user) {
        if(userRepository.findByEmail(user.getEmail()) != null){
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setActivationCode(UUID.randomUUID().toString());
        if(!user.getEmail().isEmpty()){
            String message = String.format(
                    """
                            Hello, %s\s
                            Добро пожаловать в интернет магазин мыльный кураж\s
                            Для подтверждения аккаунта перейдите по ссылке: http://localhost:8080/activate/%s""",
                    user.getEmail(), user.getActivationCode()
            );
            emailService.send(user.getEmail(), "Activation code",message);
        }
        userRepository.save(user);
        log.info("Готово к отправлению: {}", user.getEmail());
        return true;
    }
    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if(user == null){
            return false;
        }
        user.setActivationCode(null);
        user.setStatus(Status.ACTIVE);
        Cart cart = new Cart();
        user.setCart(cart);
        cartRepository.save(cart);
        userRepository.save(user);
        emailService.sendSuccessRegistration(user);
        return true;
    }
}
