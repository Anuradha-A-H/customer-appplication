package springBoot.Customer.CustomerApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springBoot.Customer.CustomerApplication.entity.Customer;
import springBoot.Customer.CustomerApplication.entity.Login;
import springBoot.Customer.CustomerApplication.entity.Role;
import springBoot.Customer.CustomerApplication.repository.CustomerRepository;
import springBoot.Customer.CustomerApplication.repository.LoginRepository;

import java.util.Optional;

@SpringBootApplication
public class CustomerApplication {


	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}
}
