package springBoot.Customer.CustomerApplication.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    private String firstName;
    private String lastName;
    private String street;
    private String address;
    private String city;
    private String state;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private Long phoneNo;



    @OneToOne
    private Login login;
}
