package springBoot.Customer.CustomerApplication.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import springBoot.Customer.CustomerApplication.entity.Customer;
import springBoot.Customer.CustomerApplication.entity.Role;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Page<Customer> findByFirstName(String name, Pageable pageable);

    Page<Customer> findByCity(String city, Pageable pageable);

    Page<Customer> findByEmail(String email, Pageable pageable);

    Page<Customer> findByPhoneNo(Long phno, Pageable pageable);
    Optional<Customer> findByEmail(String email);

}
