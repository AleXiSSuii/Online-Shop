package onlineshop.shop.controller;

import onlineshop.shop.model.Address;
import onlineshop.shop.model.User;
import onlineshop.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@PreAuthorize("hasAuthority('developers:order')")
@RequestMapping("/order")
public class OrderController {

    @Qualifier("orderService")
    @Autowired
    private OrderService orderService;

    @GetMapping("/addAddress")
    public String orderAddAddress(Model model, Address address){

        model.addAttribute("address",address);
        return "addAddress";
    }

    @PostMapping("/addAddress")
    public String orderAddAddress(Address address,Principal principal){
        orderService.addAddressToUser(address,principal);
        return "redirect:/home";
    }

    @GetMapping
    public String order(Model model,Principal principal){
        User user = orderService.getUserOfPrincipal(principal);
        if(user.getAddress()!=null){
            model.addAttribute("order", orderService.confirmOrder(principal));
            return "order";
        }else{
            return "redirect:/order/addAddress";
        }
    }
    @PostMapping
    public String order(Principal principal){
        if(principal != null){
            orderService.saveOrder(principal);
            return "redirect:/order/orderSuccess";
        }else {
            throw new UsernameNotFoundException("User is not authorize");
        }
    }
    @GetMapping("/orderSuccess")
    public String orderSuccess(Model model,Principal principal){
        User user = orderService.getUserOfPrincipal(principal);
        if(user.getAddress()!=null){
            model.addAttribute("order", orderService.confirmOrder(principal));
            return "orderSuccess";
        }else{
            return "redirect:/order/addAddress";
        }
    }

}

