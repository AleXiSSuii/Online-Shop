package onlineshop.shop.service;

import onlineshop.shop.model.*;
import onlineshop.shop.model.enums.StatusOrder;
import onlineshop.shop.repository.CartItemRepository;
import onlineshop.shop.repository.OrderRepository;
import onlineshop.shop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartService cartService;
    @Mock
    private ProductService productService;
    @Mock
    private EmailService emailService;
    @Mock
    private UserService userService;
    @InjectMocks
    private OrderService orderService;

    @Test
    void getAllOrdersFromRepository() {
        Order order1 = new Order(1L,null,null,null,null,null);
        Order order2 = new Order(2L,null,null,null,null,null);
        List<Order> allOrders = List.of(order1,order2);

        when(orderRepository.findAll()).thenReturn(allOrders);

        List<Order> result = orderService.orderList();
        assertThat(result).isEqualTo(allOrders);
        verify(orderRepository).findAll();
    }

    @Test
    void orderForIdFromRepository() {
        Order order = new Order(1L,null,null,null,null,null);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.orderGetForId(1L);
        assertThat(result).isEqualTo(order);
        verify(orderRepository).findById(1L);
    }

    @Test
    void confirmOrder() {
        Principal principal = mock(Principal.class);

        User user = mock(User.class);
        when(userService.userForPrincipal(principal)).thenReturn(user);

        Cart cart = mock(Cart.class);
        when(user.getCart()).thenReturn(cart);
        when(cart.getCartList()).thenReturn(List.of(new CartItem(), new CartItem()));
        when(cart.getFinalPrice()).thenReturn(100.0);


        Order order = orderService.confirmOrder(principal);

        assertThat(order.getUser()).isEqualTo(user);
        assertThat(order.getCartList().size()).isEqualTo(2);
        assertThat(order.getTotalPrice()).isEqualTo(100.0);
    }


    @Test
    void saveOrder() {
        Principal principal = mock(Principal.class);
        CartItem cartItem1 = new CartItem(1L,3,300,null,null,null);
        CartItem cartItem2 = new CartItem(1L,4,800,null,null,null);
        Cart cart = new Cart(1L,List.of(cartItem1,cartItem2),1100);
        User user = User.builder()
                .id(1L)
                .email("user@mail.com")
                .cart(cart)
                .orders(new ArrayList<>())
                .build();
        Order order = new Order(1L,200.0,null,null,null,null);


        when(userService.userForPrincipal(principal)).thenReturn(user);
        when(cartItemRepository.findByCart(cart)).thenReturn(cart.getCartList());

        orderService.saveOrder(principal);


        verify(cartItemRepository).save(cartItem1);
        verify(cartItemRepository).save(cartItem2);
        verify(userRepository).save(user);
        verify(cartService).clearCart(principal);
        verify(productService).checkForChangeQuantity(cart.getCartList());
    }

    @Test
    void changeHandlingStatusOnTransitStatus() {
        Order order = new Order(1L,null,null,null,null, StatusOrder.HANDLING);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.changeOrderStatus(1L,StatusOrder.TRANSIT.toString());

        verify(orderRepository).save(order);

        assertThat(order.getStatusOrder()).isEqualTo(StatusOrder.TRANSIT);
    }

    @Test
    void checkedIfQuantityCartItemBiggerThanQuantityProduct() {
        Product product1 = new Product(0L,"product1","description1",100,3,null,null,null,null);
        Product product2 = new Product(0L,"product2","description2",200,3,null,null,null,null);
        CartItem cartItem1 = new CartItem(1L,3,300,product1,null,null);
        CartItem cartItem2 = new CartItem(1L,4,800,product2,null,null);
        List<CartItem> cartItems = List.of(cartItem1,cartItem2);
        orderService.checkedQuantity(cartItems);
        assertThat(orderService.checkedQuantity(cartItems)).isEqualTo(false);
    }

    @Test
    void checkedIfOrderIsEmpty() {
        List<CartItem> orderList = List.of(new CartItem(),new CartItem());

        assertThat(orderService.checkedEmptyOrder(orderList)).isEqualTo(false);
    }
    @Test
    void checkedIfOrderIsNotEmpty() {
        List<CartItem> orderList = List.of();

        assertThat(orderService.checkedEmptyOrder(orderList)).isEqualTo(true);
    }
}