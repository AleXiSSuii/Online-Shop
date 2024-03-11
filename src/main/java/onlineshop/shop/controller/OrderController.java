package onlineshop.shop.controller;

import onlineshop.shop.model.CartItem;
import onlineshop.shop.model.Order;
import onlineshop.shop.model.User;
import onlineshop.shop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('developers:order')")
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String order(Model model, Principal principal) {
        User user = userService.userForPrincipal(principal);
        if (user.getAddress() != null) {
            model.addAttribute("order", orderService.confirmOrder(principal));
            model.addAttribute("cart", cartService.getCart(user));
            return "order";
        } else {
            return "redirect:/order/addAddress";
        }
    }

    @PostMapping
    public String order(Principal principal, Model model) {
        if (principal != null) {
            User user = userService.userForPrincipal(principal);
            List<CartItem> cartItems = user.getCart().getCartList();
            if (orderService.checkedEmptyOrder(cartItems)) {
                model.addAttribute("error", "Ваш заказ пустой");
                model.addAttribute("order", orderService.confirmOrder(principal));
                return "order";
            }
            if (!productService.checkForChangeQuantity(cartItems)) {
                model.addAttribute("error", "Такого количество продуктов на данный момент нет на складе");
                model.addAttribute("order", orderService.confirmOrder(principal));
                return "order";
            }
            if (orderService.checkedQuantity(cartItems)) {
                Order order = orderService.saveOrder(principal);
                return "redirect:/order/orderSuccess/" + order.getId();
            } else {
                model.addAttribute("error", "На складе нет такого количества товара.");
                model.addAttribute("order", orderService.confirmOrder(principal));
                return "order";
            }

        } else {
            throw new UsernameNotFoundException("User is not authorize");
        }
    }

    @GetMapping("/orderSuccess/{id}")
    public String orderSuccess(@PathVariable Long id, Model model, Principal principal) {
        User user = userService.userForPrincipal(principal);
        if (user.getAddress() != null) {
            model.addAttribute("order", orderService.orderGetForId(id));
            return "orderSuccess";
        } else {
            return "redirect:/order/addAddress";
        }
    }

    @GetMapping("/myOrders")
    public String myOrders(Model model, Principal principal) {
        User user = userService.userForPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("orders", user.getOrders());
        return "/account/myOrders";
    }

}

