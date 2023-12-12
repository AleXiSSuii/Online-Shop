package onlineshop.shop.controller;

import onlineshop.shop.model.Cart;
import onlineshop.shop.model.CartItem;
import onlineshop.shop.model.Product;
import onlineshop.shop.model.User;
import onlineshop.shop.repository.CartItemRepository;
import onlineshop.shop.repository.CartRepository;
import onlineshop.shop.repository.ProductRepository;
import onlineshop.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
@PreAuthorize("hasAuthority('developers:order')")
public class CartController {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;


    @Autowired
    public CartController(
            CartRepository cartRepository,
            ProductRepository productRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository) {

        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getCart(Model model,Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        Optional<Cart> cartOpt = cartRepository.findById(user.get().getCart().getId());
        if (cartOpt.isPresent()) {
            List<CartItem> cartItems = cartOpt.get().getCartList();
            model.addAttribute("cart", cartItems);
        } else {
            model.addAttribute("cart", new ArrayList<>());
        }
        return "cart";
    }

    @PostMapping("/add")
    public String addProductToCart(Model model, @RequestParam("product_id") Long productId, Authentication authentication) {
        System.out.println("User Authorities: " + authentication.getAuthorities());
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        Optional<Product> productOpt = productRepository.findById(productId);
        if (!productOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found");
        }
        Optional<Cart> cartOpt = cartRepository.findById(user.get().getCart().getId());
        if (!cartOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "cart doesn`t exist");
        }
        Cart cart = cartOpt.get();
        if (cart.getCartList().size() != 0) {
            for (int i = 0; i < cart.getCartList().size(); i++) {
                CartItem cartItem = cart.getCartList().get(i);
                if (productOpt.get().getId().equals(cartItem.getProduct().getId())) {
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                    cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
                    cart.setFinalPrice(cart.getCartList().stream().mapToDouble(p -> p.getPrice()).sum());
                    cart.getCartList().add(cartItem);
                    cartRepository.save(cart);
                    return "redirect:/cart";
                }
            }
        }
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(1);
        cartItem.setProduct(productOpt.get());
        cartItem.setPrice(productOpt.get().getPrice());
        cartItem.setCart(cartOpt.get());
        cart.getCartList().add(cartItem);
        cart.setFinalPrice(cart.getCartList().stream().mapToDouble(p -> p.getPrice()).sum());
        cartRepository.save(cart);
        return "redirect:/cart";
    }

    @DeleteMapping("/clear")
    public String clearCart(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        Optional<Cart> cart = cartRepository.findById(user.get().getCart().getId());
        cart.get().getCartList().clear();
        cart.get().setFinalPrice(0);
        for (int i = 0; i < cartItemRepository.count(); i++) {
            CartItem cartItem = cartItemRepository.findAll().get(i);
            if (cartItem.getCart().getId() == cart.get().getId()) {
                cartItemRepository.delete(cartItem);
            }
        }
        return "redirect:/cart";
    }

    @DeleteMapping("/delete")
    public String deleteCartItem(@RequestParam("cartItem_Id") Long cartItem_id,Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        Optional<Cart> cart = cartRepository.findById(user.get().getCart().getId());
        CartItem cartItem = cartItemRepository.findById(cartItem_id).orElseThrow();
        List<CartItem> cartItemList = cart.get().getCartList();
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItem.setPrice(cartItem.getProduct().getPrice() * (cartItem.getQuantity()));
            cartItemRepository.save(cartItem);
            cart.get().setFinalPrice(cart.get().getCartList().stream().mapToDouble(p -> p.getPrice()).sum());
            cartRepository.save(cart.get());
        } else {
            cartItemRepository.deleteById(cartItem_id);
            cart.get().setFinalPrice(0);
            cartRepository.save(cart.get());
        }
        return "redirect:/cart";
    }

    @PatchMapping("/edit")
    public String editQuantity(@RequestParam("cartItem_Id") Long cartItem_id,
                               @RequestParam("quantity") int quantity,
                               Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        Optional<Cart> cart = cartRepository.findById(user.get().getCart().getId());
        CartItem cartItem = cartItemRepository.findById(cartItem_id).orElseThrow();
        cartItem.setQuantity(quantity);
        cartItem.setPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());
        cartItemRepository.save(cartItem);
        cart.get().setFinalPrice(cart.get().getCartList().stream().mapToDouble(p -> p.getPrice()).sum());
        cartRepository.save(cart.get());
        return "redirect:/cart";
    }
}
