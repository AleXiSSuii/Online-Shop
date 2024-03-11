package onlineshop.shop.controller;

import jakarta.validation.Valid;
import onlineshop.shop.model.Address;
import onlineshop.shop.service.AddressService;
import onlineshop.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@PreAuthorize("hasAuthority('developers:order')")
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;
    @Autowired
    private UserService userService;

    @GetMapping("/addAddress")
    public String orderAddAddress(Model model, Address address) {

        model.addAttribute("address", address);
        return "addAddress";
    }

    @GetMapping("/changingAddressData")
    public String changingAddressData(Principal principal, Model model) {
        if (userService.userForPrincipal(principal).getAddress() != null) {
            model.addAttribute("address", userService.userForPrincipal(principal).getAddress());
            return "/account/changingAddressData";
        } else {
            return "redirect:/order/addAddress";
        }

    }

    @PostMapping("/addAddress")
    public String orderAddAddress(@Valid Address address, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "addAddress";
        }
        addressService.addAddressToUser(address, principal);
        return "redirect:/home";
    }

    @PatchMapping("/changingAddressData")
    public String changingAddressData(@Valid @ModelAttribute("address") Address address, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "/account/changingAddressData";
        }
        addressService.updateAddress(userService.userForPrincipal(principal).getAddress(), address);
        return "/account/changingAddressData";
    }
}
