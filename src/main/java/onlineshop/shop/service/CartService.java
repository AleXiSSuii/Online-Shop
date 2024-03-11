package onlineshop.shop.service;

import lombok.RequiredArgsConstructor;
import onlineshop.shop.model.Cart;
import onlineshop.shop.model.CartItem;
import onlineshop.shop.model.Product;
import onlineshop.shop.model.User;
import onlineshop.shop.repository.CartItemRepository;
import onlineshop.shop.repository.CartRepository;
import onlineshop.shop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserService userService;

    public Cart getCart(User user) {
        return cartRepository.findById(user.getCart().getId()).orElseThrow(() -> new NoSuchElementException("Cart not found"));
    }

    public void addProductToCart(Long productId, Principal principal) {
        User user = userService.userForPrincipal(principal);
        Product product = productRepository.findById(productId).
                orElseThrow(() -> new NoSuchElementException("Product not found"));
        Cart cart = cartRepository.findById(user.getCart().getId()).
                orElseThrow(() -> new NoSuchElementException("Cart not found"));
        if (!cart.getCartList().isEmpty()) {
            for (int i = 0; i < cart.getCartList().size(); i++) {
                CartItem cartItem = cart.getCartList().get(i);
                if (product.getId().equals(cartItem.getProduct().getId())) {
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                    cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
                    cart.setFinalPrice(cart.getCartList().stream().mapToDouble(CartItem::getPrice).sum());
                    cart.getCartList().add(cartItem);
                    cartRepository.save(cart);
                    break;
                }
            }
        }
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(1);
        cartItem.setProduct(product);
        cartItem.setPrice(product.getPrice());
        cartItem.setCart(cart);
        cart.getCartList().add(cartItem);
        cart.setFinalPrice(cart.getCartList().stream().mapToDouble(CartItem::getPrice).sum());
        cartRepository.save(cart);
    }

    public void clearCart(Principal principal) {
        User user = userService.userForPrincipal(principal);
        Cart cart = cartRepository.findById(user.getCart().getId()).
                orElseThrow(() -> new NoSuchElementException("Cart not found"));
        List<CartItem> cartItems = cartItemRepository.findAll();
        for (CartItem cartItem : cartItems) {
            Cart itemCart = cartItem.getCart();
            if (itemCart != null && (itemCart.getId().equals(cart.getId()))) {
                cartItemRepository.delete(cartItem);
            }
        }
        cart.setFinalPrice(0);
        cartRepository.save(cart);
    }

    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).
                orElseThrow(() -> new NoSuchElementException("CartItem not found"));
        Cart cart = cartRepository.findById(cartItem.getCart().getId()).
                orElseThrow(() -> new NoSuchElementException("Cart not found"));
        cart.setFinalPrice(cart.getFinalPrice() - cartItem.getPrice());
        cartItemRepository.deleteById(cartItemId);
        cartRepository.save(cart);
    }

    public void editQuantity(Long cartItemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).
                orElseThrow(() -> new NoSuchElementException("CartItem not found"));
        Cart cart = cartItem.getCart();
        cartItem.setQuantity(quantity);
        cartItem.setPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());
        cart.setFinalPrice(cart.getCartList().stream().mapToDouble(CartItem::getPrice).sum());
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }
}
