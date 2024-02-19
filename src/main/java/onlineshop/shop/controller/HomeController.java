package onlineshop.shop.controller;

import onlineshop.shop.model.Product;
import onlineshop.shop.model.ProductImage;
import onlineshop.shop.repository.CategoryRepository;
import onlineshop.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.NoSuchElementException;

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
    public String getAll(@RequestParam(value = "category", required = false) Integer id, Model model, Authentication authentication){
        List<Product> list;
        String previewImageUrl = "";
        boolean isAdmin = false;
        if(authentication != null){
            isAdmin =  authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("developers:admin"));
        }
        if (id == null) {
            list = productRepository.findAll();
        }else{
            list = productRepository.findByCategory(id);
        }
        for (Product product : list) {
            if (product.getImages() != null && !product.getImages().isEmpty()) {
                ProductImage image = product.getImages().getFirst();
                if(image.isPreviewImage()){
                    previewImageUrl = "/images/" + image.getId();
                }
            }
            product.setPreviewImageUrl(previewImageUrl);
        }
        model.addAttribute("products", list);
        model.addAttribute("categories",categoryRepository.findAll());
        model.addAttribute("isAdmin", isAdmin);
        return "catalog";
    }

    @GetMapping("/{id}")
    public String getForId(@PathVariable Long id,Model model){
        Product product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
        if(product != null){
            model.addAttribute("product",product);
            model.addAttribute("images", product.getImages());
            return "productForID";
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }
    @GetMapping("/name")
    public String getForName(@RequestParam String name,Model model){
        Product product = productRepository.findByName(name).orElseThrow(() -> new NoSuchElementException("Product not found"));
        model.addAttribute("products", productRepository.findAll());
        if(product != null){
            model.addAttribute("product",product);
            model.addAttribute("images", product.getImages());
            return "productForID";
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }
}
