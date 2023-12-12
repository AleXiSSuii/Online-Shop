package onlineshop.shop.service;

import onlineshop.shop.model.Category;
import onlineshop.shop.model.Product;
import onlineshop.shop.repository.CategoryRepository;
import onlineshop.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public void createProduct(Product product){
        productRepository.save(product);
    }
    public List<Product> allProducts(){
        return productRepository.findAll();
    }
    public Product productForId(Long id){
        return productRepository.findById(id).get();
    }
    public void updateProduct(Long id, Product product){
        Product updateProduct = productRepository.findById(id).get();
        updateProduct.setName(product.getName());
        updateProduct.setDescription(product.getDescription());
        updateProduct.setPrice(product.getPrice());
        updateProduct.setCategory(product.getCategory());
        productRepository.save(updateProduct);
    }

    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }
    public List<Category> allCategories(){
        List<Category> allCategories = categoryRepository.findAll();
        return  allCategories;
    }
    public Category categoryForId(Integer id){
        return categoryRepository.findById(id).get();
    }
    public void createCategory(Category category){
        categoryRepository.save(category);
    }
    public void deleteCategory(Integer id){
        categoryRepository.deleteById(id);
    }
    public void updateCategory(Integer id, Category category){
        Category updateCategory = categoryRepository.findById(id).get();
        updateCategory.setName(category.getName());
        categoryRepository.save(updateCategory);
    }


}
