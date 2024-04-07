package onlineshop.shop.service;

import onlineshop.shop.model.CartItem;
import onlineshop.shop.model.Category;
import onlineshop.shop.model.Product;
import onlineshop.shop.repository.CategoryRepository;
import onlineshop.shop.repository.ProductImageRepository;
import onlineshop.shop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private  ProductRepository productRepository;
    @Mock
    private  CategoryRepository categoryRepository;
    @Mock
    private  ProductImageRepository imageRepository;
    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;
    private Category category1;
    private Category category2;
    private MultipartFile file1;
    private MultipartFile file2;
    private MultipartFile file3;


    @BeforeEach
    void setUp() {
        this.category1 = new Category(1,"category1",null);
        this.category2 = new Category(2,"category2",null);
        this.product1 = new Product(1L,"Product1","Description1",500,5,category1,new ArrayList<>(),null,null);
        this.product2 = new Product(2L,"Product2","Description2",1000,10,category2,new ArrayList<>(),null,null);
        this.file1 = new MockMultipartFile("file1", "file1.jpg", "image/jpg", new byte[10]);
        this.file2 = new MockMultipartFile("file2", "file2.jpg", "image/jpg", new byte[10]);
        this.file3 = new MockMultipartFile("file3", "file3.jpg", "image/jpg", new byte[10]);
    }

    @Test
    void getAllProductsFromRepository() {
        List<Product> allProducts = List.of(product1,product2);

        when(productRepository.findAll()).thenReturn(allProducts);

        List<Product> result = productService.allProducts();
        assertThat(result).isEqualTo(allProducts);
        verify(productRepository).findAll();
    }

    @Test
    void productForIdFromRepository() {

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        Product result = productService.productForId(1L);
        assertThat(result).isEqualTo(product1);
        verify(productRepository).findById(1L);
    }

    @Test
    void createProductNewProductWithImages() throws IOException {
        Product product = new Product();

        productService.createProduct(product,file1,file2,file3);

        verify(productRepository).save(product);
        assertThat(product.getImages().size()).isEqualTo(3);
        assertThat(product.getQuantity()).isEqualTo(0);
    }

    @Test
    void deleteProduct() {
        productService.deleteProduct(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void updateProductWithImages() throws IOException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        productService.updateProduct(product1.getId(),product2,file1,file2,file3);

        assertThat(product1.getName()).isEqualTo("Product2");
        assertThat(product1.getDescription()).isEqualTo("Description2");
        assertThat(product1.getPrice()).isEqualTo(1000);
        assertThat(product1.getQuantity()).isEqualTo(10);
        assertThat(product1.getCategory()).isEqualTo(category2);
        assertThat(product1.getImages().size()).isEqualTo(3);

        verify(productRepository).save(product1);
    }

    @Test
    void getAllCategoriesFromRepository() {
        List<Category> allCategories = List.of(category1,category2);

        when(categoryRepository.findAll()).thenReturn(allCategories);

        List<Category> result = productService.allCategories();
        assertThat(result).isEqualTo(allCategories);
        verify(categoryRepository).findAll();
    }

    @Test
    void categoryForIdFromRepository() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category1));

        Category result = productService.categoryForId(1);

        assertThat(result).isEqualTo(category1);
        verify(categoryRepository).findById(1);
    }

    @Test
    void createCategory() {
        Category category = new Category(null,"category",null);

        productService.createCategory(category);

        verify(categoryRepository).save(category);
    }

    @Test

    void deleteCategory() {
        productService.deleteCategory(1);
        verify(categoryRepository).deleteById(1);
    }

    @Test
    void updateCategory() {


        when(categoryRepository.findById(1)).thenReturn(Optional.of(category1));

        productService.updateCategory(1,category2);

        assertThat(category1.getName()).isEqualTo("category2");
    }

//    @Test
//    void checkIfQuantityProductMoreThanCartItem() {
//        List<CartItem> cartItems = List.of(new CartItem(1L,3,600,product1,null,null));
//
//        boolean result = productService.checkForChangeQuantity(cartItems);
//        assertThat(result).isEqualTo(true);
//    }
//    @Test
//    void checkIfQuantityProductLessThanCartItem() {
//        List<CartItem> cartItems = List.of(
//                new CartItem(1L,3,600,product1,null,null),
//                new CartItem(2L,11,400,product2,null,null));
//        boolean result = productService.checkForChangeQuantity(cartItems);
//        assertThat(result).isEqualTo(false);
//    }
}