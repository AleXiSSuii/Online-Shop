package onlineshop.shop.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onlineshop.shop.model.User;
import onlineshop.shop.service.EmailService;
import onlineshop.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;


    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") User user){
        return "registration";
    }
    @PostMapping("/registration")
    public String createUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, HttpServletRequest request) {

        String password = user.getPassword();
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!userService.createUser(user)) {
            return "registration";
        }
        userService.authenticateUserAndSetSession(user.getEmail(),password, request);
        emailService.sendSuccessRegistration(user);
        return "redirect:/home";
    }
}
