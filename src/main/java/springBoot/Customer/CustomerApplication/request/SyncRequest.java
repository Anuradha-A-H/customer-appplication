package springBoot.Customer.CustomerApplication.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
//@Builder
@NoArgsConstructor // Add this annotation to provide a default constructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncRequest {
    private String uuid;
    private String first_name;
    private String last_name;
    private String street;
    private String address;
    private String city;
    private String state;
    private String email;
    private String phone;

}
