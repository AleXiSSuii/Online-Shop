package onlineshop.shop.service;

import lombok.RequiredArgsConstructor;
import onlineshop.shop.model.Address;
import onlineshop.shop.model.User;
import onlineshop.shop.repository.AddressRepository;
import onlineshop.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.Principal;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public void addAddressToUser(Address address, Principal principal){
        User user = userService.userForPrincipal(principal);
        user.setAddress(address);
        addressRepository.save(address);
        userRepository.save(user);
    }

    public void updateAddress(Address updateAddress, Address address) {
        if(address.getState() != null){
            updateAddress.setState(address.getState());
        }
        if(address.getCity() != null){
            updateAddress.setCity(address.getCity());
        }
        if(address.getStreet() != null){
            updateAddress.setStreet(address.getStreet());
        }
        if(address.getBuilding() != null){
            updateAddress.setBuilding(address.getBuilding());
        }
        if(address.getApartment() != null){
            updateAddress.setApartment(address.getApartment());
        }
        if(address.getPostalCode() != null){
            updateAddress.setPostalCode(address.getPostalCode());
        }

        addressRepository.save(updateAddress);

    }
}
