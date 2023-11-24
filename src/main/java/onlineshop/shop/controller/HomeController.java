package onlineshop.shop.controller;

import onlineshop.shop.model.Product;
import onlineshop.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;




@Controller
@RequestMapping("/home")
public class HomeController {
    private final ProductRepository productRepository;

    @Autowired
    public HomeController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("products",productRepository.findAll());
        return "catalog";
    }


    @GetMapping("/{id}")
    public String getForId(@PathVariable Long id,Model model){
        Optional<Product> productPersonal = productRepository.findById(id);
        if(productPersonal.isPresent()){
            Product product = productPersonal.get();
            model.addAttribute("product",product);
            return "productForID";
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }

    @PostMapping("/add")
    public Product create(@RequestBody Product product){
        productRepository.save(product);
        return product;
    }
    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id){
        Optional<Product> productPersonal = productRepository.findById(id);
        if(productPersonal.isPresent()){
            productRepository.deleteById(id);
            return "Product successfully deleted";
        }else {
            throw new IllegalArgumentException("products with id " + id + "doesn't exist");
        }
    }
    @PatchMapping("/{id}")
    public Product productEdit(@PathVariable Long id,@RequestBody Product setProduct){
        Product productForEdit = productRepository.findById(id).orElseThrow(()->new IllegalStateException(
                "person with id " + id + " does not exists"));;
        productForEdit.setName(setProduct.getName());
        productForEdit.setDescription(setProduct.getDescription());
        productForEdit.setPrice(setProduct.getPrice());
        productRepository.save(productForEdit);
        return productForEdit;
    }
}
