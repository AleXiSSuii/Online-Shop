package onlineshop.shop.controller;

import onlineshop.shop.model.Category;
import onlineshop.shop.model.Product;
import onlineshop.shop.model.User;
import onlineshop.shop.service.ProductService;
import onlineshop.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('developers:admin')")
public class AdminController {

    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public AdminController(
            UserService userService,
            ProductService productService) {
        this.productService = productService;
        this.userService = userService;
    }
    @GetMapping
    public String adminMenu(){
        return "/admin/menu";
    }

    @GetMapping("/products")
    public String getAllProducts(Model model) {
        model.addAttribute("products", productService.allProducts());
        return "/admin/product/products";
    }

    @GetMapping("/products/{id}")
    public String productGetForId(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.productForId(id));
        return "/admin/product/productForId";
    }

    @GetMapping("/products/create")
    public String newProduct(Model model, Product product) {
        model.addAttribute("product", product);
        model.addAttribute("categories", productService.allCategories());
        return "/admin/product/createProduct";
    }

    @PostMapping("/products/create")
    public String create(Product product) {
        productService.createProduct(product);
        return "redirect:/admin/products";
    }

    @DeleteMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editPage(Model model, @PathVariable("id") Long id) {
        model.addAttribute("product", productService.productForId(id));
        model.addAttribute("categories", productService.allCategories());
        return "/admin/product/editProduct";

    }

    @PatchMapping("/products/{id}")
    public String productEdit(@ModelAttribute("product") Product product, @PathVariable("id") Long id) {
        productService.updateProduct(id, product);
        return "redirect:/admin/products";
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.allUsers());

        return "/admin/user/users";
    }

    @GetMapping("/users/{id}")
    public String getForId(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.userForId(id));
        return "/admin/user/userForId";
    }

    @GetMapping("/users/edit/{id}")
    public String updateUser(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.userForId(id));
        return "/admin/user/editUser";

    }

    @PatchMapping("/users/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") Long id) {
        userService.updateUser(id, user);
        return "redirect:/admin/users";

    }

    @DeleteMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/categories")
    public String getAllCategories(Model model) {
        model.addAttribute("categories", productService.allCategories());
        return "/admin/category/categories";
    }

    @GetMapping("/categories/create")
    public String newCategory(@ModelAttribute("category") Category category) {
        return "/admin/category/createCategory";
    }

    @PostMapping("/categories/create")
    public String createCategory(Category category) {
        productService.createCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/{id}")
    public String updateCategory(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("category", productService.categoryForId(id));
        return "/admin/category/editCategory";
    }

    @PatchMapping("/categories/{id}")
    public String updateCategory(@ModelAttribute("category") Category category, @PathVariable("id") Integer id) {
        productService.updateCategory(id, category);
        return "redirect:/admin/categories";

    }

    @DeleteMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id) {
        productService.deleteCategory(id);
        return "redirect:/admin/categories";
    }
}
