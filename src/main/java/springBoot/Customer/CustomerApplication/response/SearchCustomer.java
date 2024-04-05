package springBoot.Customer.CustomerApplication.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchCustomer {

    private String key;
    private String value;
}
