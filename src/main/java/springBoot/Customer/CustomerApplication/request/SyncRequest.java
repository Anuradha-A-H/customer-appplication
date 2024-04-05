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
//    // response
//[
//    {
//        "uuid": "test11042c83ca754d30bdff545938bd793c",
//            "first_name": "Chandan ",
//            "last_name": "N P",
//            "street": "13th main road",
//            "address": "BTM Layout ",
//            "city": "Bengaluru",
//            "state": "Karnataka",
//            "email": "Chandannp2001@gmail.com",
//            "phone": "7353106540"
//    }
}
