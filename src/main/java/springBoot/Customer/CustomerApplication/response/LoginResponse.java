package springBoot.Customer.CustomerApplication.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private String userName;
    private String password;
}
