package onlineshop.shop.controller;

import onlineshop.shop.model.Category;
import onlineshop.shop.model.Product;
import onlineshop.shop.repository.CategoryRepository;
import onlineshop.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
@RestController
@RequestMapping("/admin/product")
public class AdminConroller {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Autowired
    public AdminConroller(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
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
        Category category = categoryRepository.findById(setProduct.getCategory().getId()).orElseThrow();
        productForEdit.setName(setProduct.getName());
        productForEdit.setDescription(setProduct.getDescription());
        productForEdit.setPrice(setProduct.getPrice());
        productForEdit.setCategory(category);
        productRepository.save(productForEdit);
        return productForEdit;
    }
}
