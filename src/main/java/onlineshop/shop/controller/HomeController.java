package onlineshop.shop.controller;

import onlineshop.shop.model.Product;
import onlineshop.shop.repository.CategoryRepository;
import onlineshop.shop.repository.ProductRepository;
import onlineshop.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public HomeController(ProductRepository productRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String getAll(@RequestParam(value = "category", required = false) Integer id, Model model, Authentication authentication){
        List<Product> list;
        boolean isAdmin = false;
        if(authentication != null){
            isAdmin =  authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("developers:admin"));
            System.out.println(isAdmin);
            System.out.println(authentication.getAuthorities().toString());
        }
        if (id == null) {
            list = productRepository.findAll();
        }else{
            list = productRepository.findByCategory(id);
        }
        model.addAttribute("products", list);
        model.addAttribute("categories",categoryRepository.findAll());
        model.addAttribute("isAdmin", isAdmin);
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
    @GetMapping("/name")
    public String getForName(@RequestParam String name,Model model){
        System.out.println(name);
        Optional<Product> productPersonal = productRepository.findByName(name);
        System.out.println(productPersonal.get().getName());
        model.addAttribute("products", productRepository.findAll());
        if(productPersonal.isPresent()){
            Product product = productPersonal.get();
            model.addAttribute("product",product);
            return "productForID";
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }

}
