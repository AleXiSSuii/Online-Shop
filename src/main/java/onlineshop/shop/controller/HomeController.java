package onlineshop.shop.controller;

import onlineshop.shop.model.Product;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/home")
public class HomeController {

    private List<Product> productList = Stream.of(
            new Product(1L,"Ps4","play",122.621),
            new Product(2L,"PS5","play2",255.21),
            new Product(3L,"Ps4","play",22.1))
            .collect(Collectors.toList());
    public List<Product> getProductList() {
        return this.productList;
    }

    @GetMapping
    public List<Product> getAll(){
        return productList;
    }
    @GetMapping("/{id}")
    public Product getForId(@PathVariable Long id){
        return productList.stream().filter(product -> product.getId().equals(id)).findFirst().orElse(null);

    }
    @PostMapping
    public Product create(@RequestBody Product product){
        this.productList.add(product);
        return product;
    }
    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable Long id){
        this.productList.removeIf(product -> product.getId().equals(id));

    }
    @PatchMapping("/{id}")
    public Product productEdit(@PathVariable Long id,@RequestBody Product setProduct){
        Product productEdit = productList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElseThrow(()->new IllegalStateException(
                        "person with id " + id + " does not exists"));
        productEdit.setName(setProduct.getName());
        productEdit.setDescription(setProduct.getDescription());
        productEdit.setPrice(setProduct.getPrice());
        return productEdit;
    }
}
