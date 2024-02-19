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

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private OrderService orderService;

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public boolean createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        Cart cart = new Cart();
        user.setCart(cart);
        cartRepository.save(cart);
        userRepository.save(user);
        log.info("Готово к отправлению: {}", user.getEmail());
        return true;
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

    public User userForId(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new NoSuchElementException("User not found"));
    }
    public void authenticateUserAndSetSession(String email, String password, HttpServletRequest request) {
        try {
            request.login(email, password);
        }catch (ServletException e) {
            throw new RuntimeException(e);
        }
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
}
