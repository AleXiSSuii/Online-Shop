package onlineshop.shop.controller;

import lombok.RequiredArgsConstructor;
import onlineshop.shop.model.User;
import onlineshop.shop.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }
    @PostMapping("/registration")
    public String createUser(@ModelAttribute("user") User user) {
        user.toString();
        if (!userService.createUser(user)) {
            return "registration";
        }
        return "redirect:/login";
    }
}
