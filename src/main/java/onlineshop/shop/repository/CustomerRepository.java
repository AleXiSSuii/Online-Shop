package onlineshop.shop.repository;

import onlineshop.shop.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository  extends CrudRepository<Customer, Long> {
}
