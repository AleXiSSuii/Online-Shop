package onlineshop.shop.controller;

import onlineshop.shop.model.Product;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/Cart")
public class CartController {
    HomeController homeController = new HomeController();
    List<Product> productList = homeController.getProductList();
    List<Product> productCart = new ArrayList<>();
    @GetMapping
    public List<Product> getCartProducts(){
        return this.productCart;
    }
    @GetMapping("/clear")
    public List<Product> clearCart(){
        productCart.clear();
        return productCart;
    }
    @GetMapping("/totalCost")
    public double getTotalCost(){
        return productCart.stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }
    @PostMapping("/{id}")
    public Product addProduct(@PathVariable Long id){
        Product productToAdd = productList.stream()
                .filter((p -> p.getId().equals(id)))
                .findFirst()
                .orElse(null);
        productCart.add(productToAdd);
        return productToAdd;
    }
    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable Long id){
        this.productCart.removeIf(product -> product.getId().equals(id));
    }


}
