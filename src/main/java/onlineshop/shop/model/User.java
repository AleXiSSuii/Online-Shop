package onlineshop.shop.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import onlineshop.shop.model.enums.Role;
import onlineshop.shop.model.enums.Status;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email",unique = true)
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be here")
    private String email;
    @Column(name = "number")
    @Size(min = 6, message = "Number should be bigger than 6 characters")
    private String number;
    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 1, max = 30, message = "Name should be between 1-30 characters")
    private String name;
    @Column(name = "lastname")
    @NotEmpty(message = "LastName should not be empty")
    @Size(min = 1, max = 30, message = "LastName should be between 1-30 characters")
    private String lastname;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private Status status;
    @OneToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;
    @OneToMany(mappedBy = "user")
    private List<Order> orders;
    @Column(name = "activation_code")
    private String activationCode;
}
