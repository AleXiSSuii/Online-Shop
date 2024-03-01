package onlineshop.shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "state")
    @NotEmpty(message = "State should not be empty")
    @Size(min = 3, message = "State should be more 3 characters")
    private String state;
    @Column(name = "city")
    @NotEmpty(message = "City should not be empty")
    @Size(min = 2, message = "City should be more 2 characters")
    private String city;
    @Column(name = "street")
    @NotEmpty(message = "Street should not be empty")
    @Size(min = 3, message = "Street should be more 1 characters")
    private String street;
    @Column(name = "building")
    @NotEmpty(message = "Building should not be empty")
    @Size(min = 1, message = "Building should be more 1 characters")
    private String building;
    @NotEmpty(message = "Apartment should not be empty")
    @Size(min = 1, message = "Apartment should be more 1 characters")
    @Column(name = "apartment")
    private String apartment;
    @NotEmpty(message = "PostalCode  should not be empty")
    @Size(min = 6, max = 6, message = "PostalCode should be 6 characters")
    @Column(name = "postalCode")
    private String postalCode;

}
