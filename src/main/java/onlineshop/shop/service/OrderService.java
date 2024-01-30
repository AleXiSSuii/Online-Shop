package onlineshop.shop.service;


import onlineshop.shop.model.Address;
import onlineshop.shop.model.Order;
import onlineshop.shop.model.User;
import onlineshop.shop.repository.AddressRepository;
import onlineshop.shop.repository.OrderRepository;
import onlineshop.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository, AddressRepository addressRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }
    @Qualifier("emailService")
    @Autowired
    private EmailService emailService;

    public void addAddressToUser(Address address, Principal principal){
        User user = getUserOfPrincipal(principal);
        user.setAddress(address);
        addressRepository.save(address);
        userRepository.save(user);
    }

    public User getUserOfPrincipal(Principal principal){
        return userRepository.findByEmail(principal.getName()).
                orElseThrow(() -> new UsernameNotFoundException("User with email " + principal.getName() + " not found."));
    }

    public Order confirmOrder(Principal principal){
        User user = getUserOfPrincipal(principal);
        return createOrderForUser(user);
    }

    private Order createOrderForUser(User user) {
        Order order = new Order();
        order.setUser(user);
        order.setCart(user.getCart());
        order.setTotalPrice(user.getCart().getFinalPrice());
        return order;
    }

    @Transactional
    public void saveOrder(Principal principal){
        User user = getUserOfPrincipal(principal);
        Order order = createOrderForUser(user);
        order.setDate(LocalDateTime.now());
        user.getOrders().add(order);
        orderRepository.save(order);
        userRepository.save(user);
        emailService.sendOrderConfirmation(user.getEmail(),order);
    }

    public void deleteAllOrdersForUser(User user){
        List<Order> ordersList = user.getOrders();
        for(int i = 0; i < ordersList.size(); i++){
            orderRepository.deleteById(ordersList.get(i).getId());
        }
        user.getOrders().clear();
    }
    public List<Order> orderList(){
        return orderRepository.findAll();
    }

    public List<Order> allOrdersForUser(Long id) {
        List<Order> allOrders = new ArrayList<>();
        for (long i = 0; i < orderRepository.count(); i++) {
            Optional<Order> orderOptional = orderRepository.findById(i);
            if (orderOptional.isPresent() && orderOptional.get().getUser().getId() == id) {
                allOrders.add(orderOptional.get());
            }
        }
        return allOrders;
    }
}
