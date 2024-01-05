package onlineshop.shop.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import onlineshop.shop.model.User;
import onlineshop.shop.service.EmailService;
import onlineshop.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    @Autowired
    private UserService userService;

    @Qualifier("emailService")
    @Autowired
    private EmailService emailService;


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
    public String createUser(@ModelAttribute("user") User user, HttpServletRequest request) {
        System.out.println(user.getPassword().toString());
        String password = user.getPassword();
        if (!userService.createUser(user)) {
            return "registration";
        }
        userService.authenticateUserAndSetSession(user.getEmail(),password, request);
        emailService.sendSuccessRegistration(user);
        return "redirect:/home";
    }
}
