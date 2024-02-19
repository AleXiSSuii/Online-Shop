package onlineshop.shop.controller;

import lombok.RequiredArgsConstructor;
import onlineshop.shop.model.Category;
import onlineshop.shop.model.Order;
import onlineshop.shop.model.Product;
import onlineshop.shop.model.User;
import onlineshop.shop.service.OrderService;
import onlineshop.shop.service.ProductService;
import onlineshop.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('developers:admin')")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;


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
    public String create(Product product,
                         @RequestParam("image1") MultipartFile file1,
                         @RequestParam("image2") MultipartFile file2,
                         @RequestParam("image3") MultipartFile file3) throws IOException {
        productService.createProduct(product,file1,file2,file3);
        return "redirect:/admin/products";
    }

    @DeleteMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }


    @GetMapping("/products/edit/{id}")
    public String editPage(@PathVariable("id") Long id,Model model) {
        model.addAttribute("product", productService.productForId(id));
        model.addAttribute("categories", productService.allCategories());
        return "/admin/product/editProduct";

    }

    @PatchMapping("/products/{id}")
    public String productEdit(@ModelAttribute("product") Product product,
                              @PathVariable("id") Long id,
                              @RequestParam("image1") MultipartFile file1,
                              @RequestParam("image2") MultipartFile file2,
                              @RequestParam("image3") MultipartFile file3
                              ) throws IOException {

        productService.updateProduct(id, product, file1,file2,file3);
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
    @PostMapping("users/ban/{id}")
    public String userBan(@PathVariable("id") Long id) {
        userService.banUser(id);
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

    @GetMapping("/orders")
    public String ordersMenu(Model model){
        model.addAttribute("orders", orderService.orderList());
        return "/admin/order/orders";
    }
    @GetMapping("/orders/{id}")
    public String orderForUser(@PathVariable("id") Long id ,Model model){
        model.addAttribute("order", orderService.orderGetForId(id));
        Order order = orderService.orderGetForId(id);
        return "/admin/order/orderForUser";
    }
    @PostMapping("/orders/status/{id}")
    public String changeOrderStatus(@PathVariable("id") Long id, @RequestParam("status") String newStatus) {
        orderService.changeOrderStatus(id,newStatus);
        return "redirect:/admin/orders/" + id;
    }

}
