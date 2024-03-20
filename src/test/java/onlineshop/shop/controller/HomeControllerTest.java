package onlineshop.shop.controller;

import onlineshop.shop.model.Category;
import onlineshop.shop.model.Product;
import onlineshop.shop.repository.CategoryRepository;
import onlineshop.shop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static onlineshop.shop.util.TestConstants.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")

class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private CategoryRepository categoryRepository;

    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get("/home")
                        .param("category", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("isAdmin"));
    }

    @Test
    void getForId() throws Exception {
        when(productRepository.findById(1L)).thenReturn(Optional.of(new Product(
                PRODUCT_ID,
                PRODUCT_NAME,
                PRODUCT_DESCRIPTION,
                PRODUCT_PRICE,
                PRODUCT_QUANTITY,
                new Category(),
                PRODUCT_IMAGES,
                null,
                null)));

        mockMvc.perform(get("/home/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("productForID"))
                .andExpect(model().attribute("product", hasProperty("id",is(1L))))
                .andExpect(model().attribute("product", hasProperty("name",is("NAME"))))
                .andExpect(model().attribute("product", hasProperty("description",is("DESCRIPTION"))))
                .andExpect(model().attribute("product", hasProperty("price",is(111.1))))
                .andExpect(model().attribute("product", hasProperty("quantity",is(11))));
    }

    @Test
    void getForName() {
    }
}