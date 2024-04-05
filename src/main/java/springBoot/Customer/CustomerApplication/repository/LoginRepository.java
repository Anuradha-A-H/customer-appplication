package springBoot.Customer.CustomerApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springBoot.Customer.CustomerApplication.entity.Customer;
import springBoot.Customer.CustomerApplication.entity.Login;
import springBoot.Customer.CustomerApplication.entity.Role;

import java.util.Optional;

public interface LoginRepository extends JpaRepository<Login, Long> {
    Optional<Login> findByEmail(String email);

    Optional<Login> findByRole(Role role);


}
