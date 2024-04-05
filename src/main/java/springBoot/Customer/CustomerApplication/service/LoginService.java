package springBoot.Customer.CustomerApplication.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springBoot.Customer.CustomerApplication.repository.LoginRepository;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginRepository loginRepository;



    public UserDetailsService userDetailsService(){

        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username)
            {
                return loginRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
            }
        };

    }
}
