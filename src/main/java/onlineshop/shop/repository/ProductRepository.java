package onlineshop.shop.repository;

import onlineshop.shop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("SELECT p FROM Product p WHERE p.category.id = ?1")
    List<Product> findByCategory(Integer id);
    Optional<Product> findByName(String name);


}
