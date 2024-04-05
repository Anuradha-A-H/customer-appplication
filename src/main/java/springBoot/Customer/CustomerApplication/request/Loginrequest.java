package springBoot.Customer.CustomerApplication.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Loginrequest {

    private String loginId;
    private String password;
}
