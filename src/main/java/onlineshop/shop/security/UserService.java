package onlineshop.shop.security;

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

import java.security.Principal;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
    }
    public boolean createUser(User user){
        user.toString();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        Cart cart = new Cart();
        user.setCart(cart);
        log.info("Saving new User with email: {}", user.getEmail());
        cartRepository.save(cart);
        userRepository.save(user);
        return true;
    }
    public Optional<User> getUserByPrincipal(Principal principal){
        return userRepository.findByEmail(principal.getName());
    }
}
