package onlineshop.shop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="user")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "number")
    private String number;
    @Column(name = "lastname")
    private String lastName;
    @Column(name = "name")
    private String name;
    @OneToOne(mappedBy = "user")
    private Cart cart;

}
