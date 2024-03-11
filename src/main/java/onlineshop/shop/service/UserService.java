package onlineshop.shop.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import onlineshop.shop.model.User;
import onlineshop.shop.model.enums.Status;
import onlineshop.shop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> allUsers() {
        return userRepository.findAll();
    }
    public User userForId(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new NoSuchElementException("User not found"));
    }
    public User userForPrincipal(Principal principal){
        return userRepository.findByEmail(principal.getName());
    }

    public void updateUser(Long id, User user) {
        User updateUser = userRepository.findById(id).orElseThrow(()->new NoSuchElementException("User not found"));
        if(user.getEmail() != null){
            System.out.println(user.getEmail());
            updateUser.setEmail(user.getEmail());
        }
        if(user.getName() != null){
            System.out.println(user.getName());
            updateUser.setName(user.getName());
        }
        if(user.getLastname() != null){
            System.out.println(user.getLastname());
            updateUser.setLastname(user.getLastname());
        }
        if(user.getNumber() != null){
            System.out.println(user.getNumber());
            updateUser.setNumber(user.getNumber());
        }
        if(user.getPassword() != null){
            updateUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(updateUser);
    }

    public void banUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new NoSuchElementException("User not found"));
        if (user != null) {
            if (user.getStatus().equals(Status.ACTIVE)) {
                user.setStatus(Status.BANNED);
            } else {
                user.setStatus(Status.ACTIVE);
            }
            userRepository.save(user);
        }
    }
}
