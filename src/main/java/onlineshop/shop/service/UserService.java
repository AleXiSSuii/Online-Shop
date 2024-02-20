package onlineshop.shop.service;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private OrderService orderService;
    @Autowired
    private EmailService emailService;

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public void updateUser(Long id, User user) {
        User updateUser = userRepository.findById(id).orElseThrow(()->new NoSuchElementException("User not found"));
        updateUser.setName(user.getName());
        updateUser.setLastname(user.getLastname());
        updateUser.setEmail(user.getEmail());
        updateUser.setNumber(user.getNumber());
        updateUser.setStatus(user.getStatus());
        updateUser.setRole(user.getRole());
        userRepository.save(updateUser);
    }

    public void deleteUser(Long id) {
        orderService.deleteAllOrdersForUser(userRepository.findById(id).orElseThrow(()-> new NoSuchElementException("User not found")));
        userRepository.deleteById(id);
    }

    public void banUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new NoSuchElementException("User not found"));
        if (user != null) {
            if (user.getStatus().equals(Status.ACTIVE)) {
                user.setStatus(Status.BANNED);
            } else {
                user.setStatus(Status.ACTIVE);
            }
            userRepository.save(user);
        }
    }
    public User userForId(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new NoSuchElementException("User not found"));
    }
    public User userForActivationCode(String activationCode){
        return userRepository.findByActivationCode(activationCode);
    }

}
