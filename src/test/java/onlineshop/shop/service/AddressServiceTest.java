package onlineshop.shop.service;

import onlineshop.shop.model.Address;
import onlineshop.shop.model.User;
import onlineshop.shop.repository.AddressRepository;
import onlineshop.shop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private AddressService addressService;

    @Test
    void addAddressToUser() {
        Principal principal = mock(Principal.class);
        User user = mock(User.class);
        Address address = mock(Address.class);
        when(userService.userForPrincipal(principal)).thenReturn(user);

        addressService.addAddressToUser(address,principal);

        verify(addressRepository).save(address);
        verify(userRepository).save(user);
    }

    @Test
    void updateAddress() {
        Address oldAddress = new Address(1L,"Old","Old","Old","Old","Old","Old");
        Address newAddress = new Address(1L,"New","New","New","New","New","New");

        addressService.updateAddress(oldAddress,newAddress);

        assertThat(oldAddress.getId()).isEqualTo(1L);
        assertThat(oldAddress.getState()).isEqualTo("New");
        assertThat(oldAddress.getCity()).isEqualTo("New");
        assertThat(oldAddress.getStreet()).isEqualTo("New");
        assertThat(oldAddress.getBuilding()).isEqualTo("New");
        assertThat(oldAddress.getApartment()).isEqualTo("New");
        assertThat(oldAddress.getPostalCode()).isEqualTo("New");

        verify(addressRepository).save(oldAddress);
    }
}