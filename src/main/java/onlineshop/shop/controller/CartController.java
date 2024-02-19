package onlineshop.shop.controller;

import onlineshop.shop.model.Cart;
import onlineshop.shop.model.CartItem;
import onlineshop.shop.model.ProductImage;
import onlineshop.shop.model.User;
import onlineshop.shop.service.CartService;
import onlineshop.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
@PreAuthorize("hasAuthority('developers:order')")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String getCart(Model model,Principal principal) {
        User user = cartService.getUserOfPrincipal(principal);
        String previewImageUrl = "";
        Cart cart = user.getCart();
        if (cart != null) {
            List<CartItem> cartItems = cart.getCartList();
            for (CartItem cartItem: cartItems) {
                if (cartItem.getProduct().getImages() != null && ! cartItem.getProduct().getImages().isEmpty()) {
                    ProductImage image = cartItem.getProduct().getImages().getFirst();
                    if(image.isPreviewImage()){
                        previewImageUrl = "/images/" + image.getId();
                    }
                }
                cartItem.getProduct().setPreviewImageUrl(previewImageUrl);
            }
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

    @DeleteMapping("/deleteItem")
    public String deleteCartItem(@RequestParam("cartItem_Id") Long cartItemId,Principal principal) {
        cartService.deleteCartItem(cartItemId,principal);
        return "redirect:/cart";
    }
    @PatchMapping("/edit")
    public String editQuantity(@RequestParam("cartItem_Id") Long cartItemId,
                               @RequestParam("quantity") int quantity) {
        cartService.editQuantity(cartItemId,quantity);
        return "redirect:/cart";
    }
}
