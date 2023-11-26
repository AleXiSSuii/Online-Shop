package onlineshop.shop.controller;

import onlineshop.shop.model.Category;
import onlineshop.shop.model.Product;
import onlineshop.shop.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    @Autowired
    public HomeController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String getAll(@RequestParam(value = "category", required = false) Integer id, Model model) {
        List<Product> list;
        System.out.println();
        System.out.println(id);
        System.out.println();
        if (id == null) {
            list = productRepository.findAll();
        }else{
            list = productRepository.findByCategory(id);
        }
        model.addAttribute("products", list);
        model.addAttribute("categories",categoryRepository.findAll());
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

}
