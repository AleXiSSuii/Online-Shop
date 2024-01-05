package onlineshop.shop.service;

import onlineshop.shop.model.Cart;
import onlineshop.shop.model.CartItem;
import onlineshop.shop.model.Product;
import onlineshop.shop.model.User;
import onlineshop.shop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.NoSuchElementException;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;



    @Autowired
    public CartService(CartItemRepository cartItemRepository,
                       UserRepository userRepository,
                       CartRepository cartRepository,
                       ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    public User getUserOfPrincipal(Principal principal){
        return userRepository.findByEmail(principal.getName()).
                orElseThrow(() -> new UsernameNotFoundException("User with email " + principal.getName() + " not found."));
    }



    public void addProductToCart(Long productId,Principal principal) {
        User user = getUserOfPrincipal(principal);
        Product product = productRepository.findById(productId).
                orElseThrow(()-> new NoSuchElementException("Product not found"));
        Cart cart = cartRepository.findById(user.getCart().getId()).
                orElseThrow(()-> new NoSuchElementException("Cart not found"));
        if (cart.getCartList().size() != 0) {
            for (int i = 0; i < cart.getCartList().size(); i++) {
                CartItem cartItem = cart.getCartList().get(i);
                if (product.getId().equals(cartItem.getProduct().getId())) {
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                    cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
                    cart.setFinalPrice(cart.getCartList().stream().mapToDouble(p -> p.getPrice()).sum());
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
        cart.setFinalPrice(cart.getCartList().stream().mapToDouble(p -> p.getPrice()).sum());
        cartRepository.save(cart);
    }

    public void clearCart(Principal principal){
        User user = getUserOfPrincipal(principal);
        Cart cart = cartRepository.findById(user.getCart().getId()).
                orElseThrow(()-> new NoSuchElementException("Cart not found"));
        cart.getCartList().clear();
        cart.setFinalPrice(0);
        for (int i = 0; i < cartItemRepository.count(); i++) {
            CartItem cartItem = cartItemRepository.findAll().get(i);
            if (cartItem.getCart().getId() == cart.getId()) {
                cartItemRepository.delete(cartItem);
            }
        }
    }
    public void deleteCartItem(Long cartItemId,Principal principal){
        User user = getUserOfPrincipal(principal);
        Cart cart = cartRepository.findById(user.getCart().getId()).
                orElseThrow(()-> new NoSuchElementException("Cart not found"));
        CartItem cartItem = cartItemRepository.findById(cartItemId).
                orElseThrow(()->new NoSuchElementException("CartItem not found"));
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItem.setPrice(cartItem.getProduct().getPrice() * (cartItem.getQuantity()));
            cart.setFinalPrice(cart.getCartList().stream().mapToDouble(p -> p.getPrice()).sum());
            cartItemRepository.save(cartItem);
        } else {
            cart.getCartList().remove(cartItem);
            cartItemRepository.deleteById(cartItemId);
            cart.setFinalPrice(0);
        }
        cartRepository.save(cart);
    }
    public void editQuantity(Long cartItemId,int quantity,Principal principal){
        User user = getUserOfPrincipal(principal);
        Cart cart = cartRepository.findById(user.getCart().getId()).
                orElseThrow(()-> new NoSuchElementException("Cart not found"));
        CartItem cartItem = cartItemRepository.findById(cartItemId).
                orElseThrow(()->new NoSuchElementException("CartItem not found"));
        cartItem.setQuantity(quantity);
        cartItem.setPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());
        cartItemRepository.save(cartItem);
        cart.setFinalPrice(cart.getCartList().stream().mapToDouble(p -> p.getPrice()).sum());
        cartRepository.save(cart);
    }
}
