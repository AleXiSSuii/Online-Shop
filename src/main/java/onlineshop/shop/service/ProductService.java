package onlineshop.shop.service;

import onlineshop.shop.model.CartItem;
import onlineshop.shop.model.Category;
import onlineshop.shop.model.Product;
import onlineshop.shop.model.ProductImage;
import onlineshop.shop.repository.CategoryRepository;
import onlineshop.shop.repository.ProductImageRepository;
import onlineshop.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository imageRepository;


    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = productImageRepository;
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
        product.setPreviewImageId(product.getImages().getFirst().getId());
        product.setQuantity(0);
        productRepository.save(product);
    }

    public List<Product> allProducts() {
        return productRepository.findAll();
    }

    public Product productForId(Long id) {
        return productRepository.findById(id).orElseThrow(()->new NoSuchElementException("Product not found"));
    }

    public void updateProduct(Long id, Product product, MultipartFile file1,MultipartFile file2,MultipartFile file3) throws IOException {
        Product updateProduct = productRepository.findById(id).orElse(null);
        if (updateProduct != null) {
            updateProduct.setName(product.getName());
            updateProduct.setDescription(product.getDescription());
            updateProduct.setPrice(product.getPrice());
            updateProduct.setCategory(product.getCategory());
            updateProduct.setQuantity(product.getQuantity());
            if (file1.getSize() != 0) {
                ProductImage image1 = toImageEntity(file1);
                image1.setPreviewImage(true);
                if(!updateProduct.getImages().isEmpty()){
                    changeImage(updateProduct.getImages().getFirst().getId(),image1);
                    updateProduct.setPreviewImageId(updateProduct.getImages().getFirst().getId());
                }else {
                    updateProduct.addImageToProduct(image1);
                }
            }
            if (file2.getSize() != 0) {
                ProductImage image2 = toImageEntity(file2);
                image2.setPreviewImage(false);
                if(updateProduct.getImages().size()>1){
                    changeImage(updateProduct.getImages().get(1).getId(),image2);
                }else {
                    updateProduct.addImageToProduct(image2);
                }
            }
            if (file3.getSize() != 0) {
                ProductImage image3 = toImageEntity(file3);
                image3.setPreviewImage(false);
                if(updateProduct.getImages().size()>2){
                    changeImage(updateProduct.getImages().get(2).getId(),image3);
                }else {
                    updateProduct.addImageToProduct(image3);
                }
            }
            productRepository.save(updateProduct);
        }
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Category> allCategories() {
        return categoryRepository.findAll();
    }

    public Category categoryForId(Integer id) {
        return categoryRepository.findById(id).orElseThrow(()-> new NoSuchElementException("Category not found") );
    }

    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }

    public void updateCategory(Integer id, Category category) {
        Category updateCategory = categoryRepository.findById(id).orElseThrow(()-> new NoSuchElementException("Category not found"));
        updateCategory.setName(category.getName());
        categoryRepository.save(updateCategory);
    }
    public boolean changeQuantity(List<CartItem> cartList){
        for (CartItem cartItem : cartList) {
            Product product = cartItem.getProduct();
            if (product.getQuantity() - cartItem.getQuantity() >= 0) {
                product.setQuantity(product.getQuantity() - cartItem.getQuantity());
                productRepository.save(product);
            } else {
                return false;
            }
        }
        return true;
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
    private void changeImage(Long id,ProductImage newImage){
        ProductImage oldImage = imageRepository.findById(id).orElseThrow();
        oldImage.setName(newImage.getName());
        oldImage.setImage(newImage.getImage());
        oldImage.setContentType(newImage.getContentType());
        oldImage.setSize(newImage.getSize());
        oldImage.setUploadDate(LocalDate.now());
        imageRepository.save(oldImage);
    }
}
