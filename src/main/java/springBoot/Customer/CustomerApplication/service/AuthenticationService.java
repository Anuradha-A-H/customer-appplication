package springBoot.Customer.CustomerApplication.service;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springBoot.Customer.CustomerApplication.entity.Login;
import springBoot.Customer.CustomerApplication.entity.Role;
import springBoot.Customer.CustomerApplication.repository.CustomerRepository;
import springBoot.Customer.CustomerApplication.repository.LoginRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private  final LoginRepository userRepository;
    private final CustomerRepository customerRepository;


    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public String signin(Login request) throws Exception {
        List<Login> logindtl = userRepository.findAll();
        if(logindtl.size() == 0)
        {
            Login log = Login.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(log);
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Invalid Email Or Password.");
        }

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid Email Or Password."));
        if(user.getRole() == Role.CUSTOMER)
              throw new Exception("Customer  is not allowed to view/ update all user");
        var jwt = jwtService.generateToken(user);
        return jwt;

    }



}
