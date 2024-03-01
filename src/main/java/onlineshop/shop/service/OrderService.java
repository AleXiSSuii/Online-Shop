package onlineshop.shop.service;


import onlineshop.shop.model.CartItem;
import onlineshop.shop.model.Order;
import onlineshop.shop.model.User;
import onlineshop.shop.model.enums.StatusOrder;
import onlineshop.shop.repository.CartItemRepository;
import onlineshop.shop.repository.OrderRepository;
import onlineshop.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
    }
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;

    public Order confirmOrder(Principal principal){
        User user = userService.getUserOfPrincipal(principal);
        return createOrderForUser(user);
    }

    private Order createOrderForUser(User user) {
        Order order = new Order();
        order.setUser(user);
        List<CartItem> cartListCopy = new ArrayList<>(user.getCart().getCartList());
        order.setCartList(cartListCopy);
        order.setTotalPrice(user.getCart().getFinalPrice());
        return order;
    }

    @Transactional
    public Order saveOrder(Principal principal){
        User user = userService.getUserOfPrincipal(principal);
        Order order = createOrderForUser(user);
        order.setDate(LocalDateTime.now());
        List<CartItem> cartList = new ArrayList<>(user.getCart().getCartList());
        List<CartItem> userCartItems = cartItemRepository.findByCart(user.getCart());
        userCartItems.forEach(cartItem -> {
            cartItem.setOrder(order);
            cartItemRepository.save(cartItem);
        });
        order.setCartList(cartList);
        order.setStatusOrder(StatusOrder.HANDLING);
        user.getOrders().add(order);
        orderRepository.save(order);
        userRepository.save(user);
        emailService.sendOrderConfirmation(user.getEmail(),order);
        cartService.clearCart(principal);
        productService.changeQuantity(cartList);
        return order;
    }

    public List<Order> orderList(){
        return orderRepository.findAll();
    }

    public Order orderGetForId(Long id){
        return orderRepository.findById(id).orElseThrow();
    }

    public void changeOrderStatus(Long id,String newStatus){
        Order order = orderRepository.findById(id).orElseThrow();
        order.setStatusOrder(StatusOrder.valueOf(newStatus));
        orderRepository.save(order);
    }
    public boolean checkedQuantity(List<CartItem> cart){
        for (CartItem cartItem : cart) {
            if (cartItem.getQuantity() > cartItem.getProduct().getQuantity()) {
                return false;
            }
        }
        return true;
    }
    public boolean checkedEmptyOrder(List<CartItem> cart){
        return cart.isEmpty();
    }

}
