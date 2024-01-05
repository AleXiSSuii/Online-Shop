package onlineshop.shop.service;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import onlineshop.shop.model.Cart;
import onlineshop.shop.model.User;
import onlineshop.shop.model.enums.Role;
import onlineshop.shop.model.enums.Status;
import onlineshop.shop.repository.CartRepository;
import onlineshop.shop.repository.UserRepository;
import onlineshop.shop.security.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
    }

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    @Qualifier("orderService")
    private OrderService orderService;

    @Autowired
    private AuthenticationManager authenticationManager;



    public List<User> allUsers() {

        return userRepository.findAll();
    }

    public boolean createUser(User user) {
        user.toString();
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

    public Optional<User> getUserByPrincipal(Principal principal) {
        return userRepository.findByEmail(principal.getName());
    }

    public void updateUser(Long id, User user) {
        User updateUser = userRepository.findById(id).get();
        updateUser.setName(user.getName());
        updateUser.setLastname(user.getLastname());
        updateUser.setEmail(user.getEmail());
        updateUser.setNumber(user.getNumber());
        updateUser.setStatus(user.getStatus());
        updateUser.setRole(user.getRole());
        userRepository.save(updateUser);
    }

    public void deleteUser(Long id) {
        orderService.deleteAllOrdersForUser(userRepository.findById(id).get());
        userRepository.deleteById(id);
    }

    public User userForId(Long id) {
        User user = userRepository.findById(id).get();
        return user;
    }
    public void authenticateUserAndSetSession(String email,String password, HttpServletRequest request) {
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email,password);
        request.getSession(true);
        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }

}
