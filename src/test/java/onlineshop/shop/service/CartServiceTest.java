package onlineshop.shop.service;

import onlineshop.shop.model.Cart;
import onlineshop.shop.model.CartItem;
import onlineshop.shop.model.Product;
import onlineshop.shop.model.User;
import onlineshop.shop.repository.CartItemRepository;
import onlineshop.shop.repository.CartRepository;
import onlineshop.shop.repository.ProductRepository;
import onlineshop.shop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private CartService cartService;

    @Test
    void checkAddProductToCartIfCartHaveTwoElement(){
        Principal principal = mock(Principal.class);
        User user = mock(User.class);
        Product product1 = new Product(0L, "product1", "description", 100, null, null, null, null, null);
        Product product2 = new Product(1L, "product2", "description", 200, null, null, null, null, null);
        Cart cart = new Cart(0L, new ArrayList<>(),0);
        CartItem cartItem1 = new CartItem(0L, 1, 200,product2,cart,null);
        cart.getCartList().add(cartItem1);

        when(userService.userForPrincipal(principal)).thenReturn(user);
        when(user.getCart()).thenReturn(cart);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product1));
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));

        cartService.addProductToCart(product1.getId(),principal);

        verify(cartRepository).save(cart);
        assertThat(cart.getFinalPrice()).isEqualTo(300);
        assertThat(cart.getCartList().size()).isEqualTo(2);
    }

    @Test
    void checkAddProductToCartIfCartEmpty(){
        Principal principal = mock(Principal.class);
        User user = mock(User.class);
        Product product = new Product(0L, "product", "description", 100, null, null, null, null, null);
        Cart cart = new Cart(0L, new ArrayList<>(),0);

        when(userService.userForPrincipal(principal)).thenReturn(user);
        when(user.getCart()).thenReturn(cart);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));

        cartService.addProductToCart(product.getId(),principal);

        verify(cartRepository).save(cart);
        assertThat(cart.getFinalPrice()).isEqualTo(100);
        assertThat(cart.getCartList().size()).isEqualTo(1);
    }

    @Test
    void checkedClearCartFromAllItems(){
        Principal principal = mock(Principal.class);
        User user = mock(User.class);
        CartItem cartItem1 = new CartItem(1L, 3, 300, null,null , null);
        CartItem cartItem2 = new CartItem(0L, 5, 1000, null,null , null);
        CartItem cartItem3 = new CartItem(2L, 3, 500, null,null , null);
        Cart cart = new Cart(1L,List.of(cartItem1, cartItem2),1500);
        when(userService.userForPrincipal(principal)).thenReturn(user);
        when(user.getCart()).thenReturn(cart);
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        List<CartItem> cartItems = List.of(
                cartItem1,
                cartItem2,
                cartItem3
        );

        when(cartItemRepository.findAll()).thenReturn(cartItems);

        cartService.clearCart(principal);

        verify(cartRepository).save(cart);
    }

    @Test
    void checkedIfDeleteCartItemFromCart() {
        CartItem cartItem = new CartItem(0L, 3, 300, null,null , null);
        Cart cart = new Cart(0L, List.of(cartItem), 300);
        cartItem.setCart(cart);
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));
        cartService.deleteCartItem(0L);
        verify(cartItemRepository).deleteById(0L);
        assertThat(cart.getFinalPrice()).isEqualTo(0);
        assertThat(cart.getCartList().size()).isEqualTo(1);
    }

    @Test
    void checkedChangeQuantityForCartItem() {
        Product product = new Product(0L, "product", "description", 100, null, null, null, null, null);
        CartItem cartItem = new CartItem(0L, 3, 300, product, null, null);
        Cart cart = new Cart(null,List.of(cartItem),300);
        cartItem.setCart(cart);

        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));

        cartService.editQuantity(cartItem.getId(), 5);

        verify(cartItemRepository).save(cartItem);
        assertThat(cartItem.getQuantity()).isEqualTo(5);
        assertThat(cartItem.getPrice()).isEqualTo(500);
        verify(cartRepository).save(cart);
        assertThat(cart.getFinalPrice()).isEqualTo(500);
    }
}