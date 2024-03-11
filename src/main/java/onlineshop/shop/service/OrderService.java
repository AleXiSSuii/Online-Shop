package onlineshop.shop.service;

import lombok.RequiredArgsConstructor;
import onlineshop.shop.model.CartItem;
import onlineshop.shop.model.Order;
import onlineshop.shop.model.User;
import onlineshop.shop.model.enums.StatusOrder;
import onlineshop.shop.repository.CartItemRepository;
import onlineshop.shop.repository.OrderRepository;
import onlineshop.shop.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final ProductService productService;
    private final EmailService emailService;
    private final UserService userService;

    public List<Order> orderList(){
        return orderRepository.findAll();
    }

    public Order orderGetForId(Long id){
        return orderRepository.findById(id).orElseThrow();
    }

    public Order confirmOrder(Principal principal){
        User user = userService.userForPrincipal(principal);
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
        User user = userService.userForPrincipal(principal);
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
        productService.checkForChangeQuantity(cartList);
        return order;
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
