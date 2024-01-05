package onlineshop.shop.controller;

import onlineshop.shop.model.Cart;
import onlineshop.shop.model.CartItem;
import onlineshop.shop.model.Product;
import onlineshop.shop.model.User;
import onlineshop.shop.repository.CartItemRepository;
import onlineshop.shop.repository.CartRepository;
import onlineshop.shop.repository.ProductRepository;
import onlineshop.shop.repository.UserRepository;
import onlineshop.shop.service.CartService;
import onlineshop.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
@PreAuthorize("hasAuthority('developers:order')")
public class CartController {

    @Qualifier("cartService")
    @Autowired
    private CartService cartService;

    @GetMapping
    public String getCart(Model model,Principal principal) {
        User user = cartService.getUserOfPrincipal(principal);
        Cart cart = user.getCart();
        if (cart != null) {
            List<CartItem> cartItems = cart.getCartList();
            model.addAttribute("cart", cartItems);
        } else {
            model.addAttribute("cart", new ArrayList<>());
        }
        return "cart";
    }

    @PostMapping("/add")
    public String addProductToCart(@RequestParam("product_id") Long productId, Principal principal) {
        cartService.addProductToCart(productId,principal);
        return "redirect:/cart";
    }

    @DeleteMapping("/clear")
    public String clearCart(Principal principal) {
        cartService.clearCart(principal);
        return "redirect:/cart";
    }

    @DeleteMapping("/delete")
    public String deleteCartItem(@RequestParam("cartItem_Id") Long cartItemId,Principal principal) {
        cartService.deleteCartItem(cartItemId,principal);
        return "redirect:/cart";
    }

    @PatchMapping("/edit")
    public String editQuantity(@RequestParam("cartItem_Id") Long cartItemId,
                               @RequestParam("quantity") int quantity,
                               Principal principal) {
        cartService.editQuantity(cartItemId,quantity,principal);
        return "redirect:/cart";
    }
}
