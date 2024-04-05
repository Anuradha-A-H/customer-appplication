package springBoot.Customer.CustomerApplication.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import springBoot.Customer.CustomerApplication.entity.Customer;
import springBoot.Customer.CustomerApplication.entity.Login;
import springBoot.Customer.CustomerApplication.entity.Role;
import springBoot.Customer.CustomerApplication.repository.CustomerRepository;
import springBoot.Customer.CustomerApplication.repository.LoginRepository;
import springBoot.Customer.CustomerApplication.request.SyncRequest;
import springBoot.Customer.CustomerApplication.response.LoginResponse;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private LoginRepository loginRepo;
    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Value("${authentication.api.url}")
    private String authenticationApiUrl;

    @Value("${customer.list.api.url}")
    private String customerListApiUrl;

// check invalid customerId if invalid throw error if email already exists update it
    // else add  new login details and customer details to database
    public LoginResponse addCustomer(Customer signUpRequest) throws Exception {
        if(signUpRequest.getCustomerId() != null && signUpRequest.getCustomerId()<=0)
        {
            customerRepo.save(signUpRequest);
            throw new Exception("Somthing went wrong");
        }else {


            Optional<Customer> logindtl = customerRepo.findByEmail(signUpRequest.getEmail());
            if (!logindtl.isEmpty()) {
                Customer customer = logindtl.get();
                signUpRequest.setCustomerId(customer.getCustomerId());
                customerRepo.save(signUpRequest);
                return null;
            }

            String[] pass = signUpRequest.getEmail().split("@");

            Customer customer = Customer.builder()
                    .firstName(signUpRequest.getFirstName())
                    .lastName(signUpRequest.getLastName())
                    .street(signUpRequest.getStreet())
                    .address(signUpRequest.getAddress())
                    .city(signUpRequest.getCity())
                    .state(signUpRequest.getState())
                    .email(signUpRequest.getEmail())
                    .phoneNo(signUpRequest.getPhoneNo())

                    .build();
            Customer customerdtl = customerRepo.save(customer);
            Login login = Login.builder()
                    .email(signUpRequest.getEmail())
                    .password(passwordEncoder.encode(pass[0] + "@123"))
                    .role(Role.CUSTOMER)
                    .customer(customerdtl)
                    .build();
            Login logdtl = loginRepo.save(login);
            customerdtl.setLogin(logdtl);
            customerRepo.save(customer);

            return LoginResponse.builder().password(pass[0] + "@123").userName(logdtl.getUsername()).build();
        }
    }



    // get all customer with pagination
    public Page<Customer> getAllCustomer(Integer pageNumber, Integer pageSize){

        if(pageNumber<0)
            pageNumber  = 0;
        Sort sort = Sort.by(Sort.Direction.ASC, "customerId");
        Pageable p = PageRequest.of(pageNumber,pageSize, sort);

        return  customerRepo.findAll(p);
    }

    public Customer findByCustomerId(Long id) throws Exception
    {
        Optional<Customer> customer =  customerRepo.findById(id);
        if(customer.isEmpty())
             throw new Exception("Invalid CustomerId");
        return customer.get();
    }
    public void updateCustomer(Customer customer){
        customerRepo.save(customer);

    }
    public void deleteCustomer(Long id){
        Optional<Customer> cust =  customerRepo.findById(id);

        if(cust.isEmpty())
            return;
        Customer customer = cust.get();
        Login login = customer.getLogin();
        // Remove the association between customer and login
        customer.setLogin(null);

        // Save the customer to update the association
        customerRepo.save(customer);

        // Now  safely delete the login record
        loginRepo.delete(login);

        // Finally, delete the customer record
        customerRepo.delete(customer);
    }

// added pagination in search and sort based on key
    public Page<Customer> searchCustomer(String key, String val, Integer pageNumber, Integer pageSize)
    {
        Page<Customer> resultPage;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        resultPage = switch (key) {
            case "name" -> customerRepo.findByFirstName(val, pageRequest);
            case "city" -> customerRepo.findByCity(val, pageRequest);
            case "email" -> customerRepo.findByEmail(val, pageRequest);
            case "phone" -> customerRepo.findByPhoneNo(Long.parseLong(val), pageRequest);
            default -> Page.empty();
        };
        return resultPage;
    }


    public void syncCustomer() throws IOException {
        String requestBody = "{\"login_id\": \"test@sunbasedata.com\", \"password\": \"Test@123\"}";

        // Set request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create HTTP entity with request body and headers
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // Making POST request to authentication API
        ResponseEntity<String> responseEntity = new RestTemplate().postForEntity(authenticationApiUrl, requestEntity, String.class);

        // Check if response is successful (status code 200 OK)
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // Extract access token from response body
            String responseBody = responseEntity.getBody();
            String bearerToken = extractAccessToken(responseBody);

            // Set request headers
            HttpHeaders listHeaders = new HttpHeaders();
            // add token which is received from login api
            listHeaders.set("Authorization", "Bearer "+bearerToken);
            // Create HTTP entity with request body and headers
            HttpEntity<String> listRequestEntity = new HttpEntity<>(null, listHeaders);
            // Making Get request to With Authentication API
            ResponseEntity<String> listResponseEntity = new RestTemplate().exchange(customerListApiUrl, HttpMethod.GET, listRequestEntity, String.class);

            if (listResponseEntity.getStatusCode() != HttpStatus.OK) {
                throw new IOException("Some thing went wrong");
            }
            // get bosy from response
            String customerListJson = listResponseEntity.getBody();

            // response in json format converting to list
            List<SyncRequest> customers = parseCustomerListJson(customerListJson);
            bulkStore(customers);// add customer
        }
    }

    private String extractAccessToken(String responseBody) {
        // response body is in the given format and access token is directly available
        return responseBody.substring(responseBody.indexOf(':') + 2, responseBody.length() - 3);
    }

    private void bulkStore(List<SyncRequest> cust)
    {
        for(SyncRequest s : cust)
        {
            if (s.getEmail() == null || s.getEmail().isEmpty() || !s.getPhone().matches("\\d+") || !s.getEmail().endsWith("@gmail.com")) {

                continue; // Skip if email is empty || phone number is not number || not ending with @gmail.com
            }
            Optional<Customer> customer = customerRepo.findByEmail(s.getEmail());
            if(customer.isEmpty())
            {

                // email does not exist so create new customer
                createNewCustomer(s);
            } else {
                // email already exist so update the record
                updateExistingCustomer(s, customer.get());
            }
        }
    }

    public List<SyncRequest> parseCustomerListJson(String customerListJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Define the type of collection is needed to map
        CollectionType listType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, SyncRequest.class);

        // Map the JSON string to a list of SyncRequest objects

        return objectMapper.readValue(customerListJson, listType);
    }

    private void createNewCustomer(SyncRequest s) {
        String[] stringList = s.getEmail().split("@");
        String encodedPassword = passwordEncoder.encode(stringList[0] + "@123");

        Login login = Login.builder()
                .role(Role.CUSTOMER)
                .email(s.getEmail())
                .password(encodedPassword)
                .build();
        Login savedLogin = loginRepo.save(login);

        Customer customer = Customer.builder()
                .email(s.getEmail())
                .state(s.getState())
                .city(s.getCity())
                .street(s.getStreet())
                .address(s.getAddress())
                .lastName(s.getLast_name())
                .firstName(s.getFirst_name())
                .login(savedLogin)
                .phoneNo(Long.parseLong(s.getPhone()))
                .build();
        savedLogin.setCustomer(customer);
        customerRepo.save(customer);
    }

    private void updateExistingCustomer(SyncRequest s, Customer existingCustomer) {
        existingCustomer.setCity(s.getCity());
        existingCustomer.setAddress(s.getAddress());
        existingCustomer.setFirstName(s.getFirst_name());
        existingCustomer.setLastName(s.getLast_name());
        existingCustomer.setPhoneNo(Long.parseLong(s.getPhone()));
        existingCustomer.setState(s.getState());
        existingCustomer.setStreet(s.getStreet());
        customerRepo.save(existingCustomer);
    }

}
