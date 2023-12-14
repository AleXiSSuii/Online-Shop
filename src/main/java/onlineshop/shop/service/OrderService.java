package onlineshop.shop.service;

import onlineshop.shop.controller.CartController;
import onlineshop.shop.model.Address;
import onlineshop.shop.model.Order;
import onlineshop.shop.model.User;
import onlineshop.shop.repository.AddressRepository;
import onlineshop.shop.repository.OrderRepository;
import onlineshop.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.Principal;
import java.time.LocalDateTime;

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

    }
}
