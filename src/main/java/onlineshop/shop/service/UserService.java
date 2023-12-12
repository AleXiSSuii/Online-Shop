package onlineshop.shop.service;

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
import java.util.List;
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
    public List<User> allUsers(){
        return userRepository.findAll();
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
    public void updateUser(Long id,User user){
        User updateUser = userRepository.findById(id).get();
        updateUser.setName(user.getName());
        updateUser.setLastname(user.getLastname());
        updateUser.setEmail(user.getEmail());
        updateUser.setNumber(user.getNumber());
        updateUser.setStatus(user.getStatus());
        updateUser.setRole(user.getRole());
        userRepository.save(updateUser);
    }
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
    public User userForId(Long id){
        User user = userRepository.findById(id).get();
        return user;
    }
}
