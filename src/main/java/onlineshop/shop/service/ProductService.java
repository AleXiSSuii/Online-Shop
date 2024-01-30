package onlineshop.shop.service;

import onlineshop.shop.model.Category;
import onlineshop.shop.model.Product;
import onlineshop.shop.model.ProductImage;
import onlineshop.shop.repository.CategoryRepository;
import onlineshop.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
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

    public void createProduct(Product product,
                              MultipartFile file1,
                              MultipartFile file2,
                              MultipartFile file3) throws IOException {
        ProductImage image1;
        ProductImage image2;
        ProductImage image3;
        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            product.addImageToProduct(image1);
        }
        if (file3.getSize() != 0) {
            image2 = toImageEntity(file2);
            image2.setPreviewImage(false);
            product.addImageToProduct(image2);
        }
        if (file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            image3.setPreviewImage(false);
            product.addImageToProduct(image3);
        }
        product.setPreviewImageId(product.getImages().get(0).getId());
        productRepository.save(product);
    }

    private ProductImage toImageEntity(MultipartFile file) throws IOException {
        ProductImage image = new ProductImage();
        image.setName(file.getName());
        image.setImage(file.getBytes());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setUploadDate(LocalDate.now());
        return image;
    }

    public List<Product> allProducts() {
        return productRepository.findAll();
    }

    public Product productForId(Long id) {
        return productRepository.findById(id).get();
    }

    public void updateProduct(Long id, Product product, MultipartFile file) throws IOException {
        Product updateProduct = productRepository.findById(id).get();
        updateProduct.setName(product.getName());
        updateProduct.setDescription(product.getDescription());
        updateProduct.setPrice(product.getPrice());
        updateProduct.setCategory(product.getCategory());
        productRepository.save(updateProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Category> allCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        return allCategories;
    }

    public Category categoryForId(Integer id) {
        return categoryRepository.findById(id).get();
    }

    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }

    public void updateCategory(Integer id, Category category) {
        Category updateCategory = categoryRepository.findById(id).get();
        updateCategory.setName(category.getName());
        categoryRepository.save(updateCategory);
    }
}
