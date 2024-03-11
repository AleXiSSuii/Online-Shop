package onlineshop.shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onlineshop.shop.model.User;
import onlineshop.shop.service.RegistrationService;
import onlineshop.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private UserService userService;


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") User user) {
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!registrationService.createUser(user)) {
            model.addAttribute("error", "Пользователь с таким email уже существует.");
            return "registration";
        }
        return "login";
    }

    @GetMapping("/activate/{code}")
    public String activateUser(@PathVariable("code") String code,
                               Model model) {
        boolean isActivated = registrationService.activateUser(code);
        if (isActivated) {
            model.addAttribute("Success", "Активация прошла успешна");
        } else {
            model.addAttribute("Error", "Произошла ошибка во время активации");
        }
        return "redirect:/login?activated=true";
    }


    @GetMapping("/user/getAccount")
    @PreAuthorize("hasAuthority('developers:order')")
    public String getAccount() {
        return "/account/menu";
    }

    @GetMapping("/user/changingUserData")
    @PreAuthorize("hasAuthority('developers:order')")
    public String changingUserData(Principal principal,Model model) {
        model.addAttribute("user", userService.userForPrincipal(principal));
        return "/account/changingUserData";
    }

    @PatchMapping("/user/changingUserData")
    public String changingUserData(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,Principal principal) {
        if(bindingResult.hasErrors()){
            return "/account/changingUserData";
        }
        userService.updateUser(userService.userForPrincipal(principal).getId(), user);
        return "redirect:/user/changingUserData";
    }
}
