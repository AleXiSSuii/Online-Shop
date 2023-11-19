package onlineshop.shop.controller;

import onlineshop.shop.model.CartItem;
import onlineshop.shop.model.Product;
import onlineshop.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/CartItem")
public class CartItemController {
    private ProductRepository productRepository;
    @Autowired
    private CartController cartController;

    public CartItemController(CartController cartController) {
        this.cartController = cartController;
    }

    @Autowired
    public CartItemController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    private List<CartItem> listCart = new ArrayList<>();
    @PostMapping("/addItem/{id}")
    public CartItem addCartItem(@PathVariable Long id){
        Product product = productRepository.findById(id).orElseThrow();
        for(int i = 0; i < listCart.size();i++){
            CartItem cartItem = listCart.get(i);
            if(product.getId().equals(cartItem.getProduct().getId())){
                cartItem.setQuantity(cartItem.getQuantity()+1);
                cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
                listCart.add(cartItem);
                return cartItem;
            }
        }
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(1);
        cartItem.setProduct(product);
        cartItem.setPrice(product.getPrice());
        cartController.list.add(cartItem);
        listCart.add(cartItem);
        return cartItem;
    }
}
