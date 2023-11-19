package onlineshop.shop.controller;

import onlineshop.shop.model.CartItem;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/Cart")
public class CartController {
    public List<CartItem> list = new ArrayList<>();

    @GetMapping
    public List<CartItem> getAll(){
        return this.list;
    }

    @GetMapping("/totalPrice")
    public double getTotalPrice(){
        return this.list.stream()
                .mapToDouble(cartItem -> cartItem.getPrice())
                .sum();
    }

}
