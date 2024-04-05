package springBoot.Customer.CustomerApplication.controller;


import com.sun.net.httpserver.Headers;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springBoot.Customer.CustomerApplication.entity.Customer;
import springBoot.Customer.CustomerApplication.request.Loginrequest;
import springBoot.Customer.CustomerApplication.response.LoginResponse;
import springBoot.Customer.CustomerApplication.service.CustomerService;

import java.io.IOException;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Value("${authentication.api.url}")
    private String authenticationApiUrl;

    @Value("${customer.list.api.url}")
    private String customerListApiUrl;


    @GetMapping
    public String getAllCustomer(@RequestParam( value = "pageNumber",defaultValue = "1",required = false) Integer pageNumber,
                                 @RequestParam( value = "pageSize",defaultValue = "3",required = false) Integer pageSize,
                                 @RequestParam(value = "key", required = false) String key,
                                 @RequestParam(value = "value", required = false) String value,

                                 Model m){

       Page<Customer> page;

    if (key != null && value != null && !key.equals("") && !value.equals("")) {

        // If search parameters are provided, perform search
        page = customerService.searchCustomer(key, value, pageNumber - 1, pageSize);
    } else {

        // If no search parameters, just fetch paginated data
        page = customerService.getAllCustomer(pageNumber - 1, pageSize);
    }
        m.addAttribute("list", page.getContent());
        m.addAttribute("page", page);
        return "Customer";
    }



    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id)
    {
        customerService.deleteCustomer(id);
        return "redirect:/customer";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id,Model m, RedirectAttributes redirectAttributes)
    {
        try{
            Customer customer = customerService.findByCustomerId(id);
            m.addAttribute("customer",customer);
            return "addcustomer";
        }catch(Exception e){

            redirectAttributes.addFlashAttribute("success",e.getMessage());

            return "redirect:/customer";
        }

    }


    @PostMapping("/addcustomer")
    public String addCustomer(@ModelAttribute Customer request,Model m, RedirectAttributes redirectAttributes){
        try {
            LoginResponse logindtl = customerService.addCustomer(request);

            if (logindtl == null) {
                redirectAttributes.addFlashAttribute("success", "Customer updated");
            }else{
                redirectAttributes.addFlashAttribute("success", "Customer added successfully, username: " + logindtl.getUserName() + " password: " + logindtl.getPassword());

                }
            return "redirect:/customer"; // Redirect to the customer page
        } catch (Exception e) {
            // Handle unexpected errors
            m.addAttribute("error", e.getMessage());
            return "addcustomer";
        }
    }

    @GetMapping("addcustomerview")
    public String customerAddView(HttpSession session)
    {

        return "addcustomer";
    }

    @GetMapping("/sync/customers")
    public String syncCustomers(RedirectAttributes redirectAttributes) {
        try{
            customerService.syncCustomer();
            redirectAttributes.addFlashAttribute("success", "Customers Addedd successfully");

            return "redirect:/customer";
        }catch (IOException e)
        {

            redirectAttributes.addFlashAttribute("error", e.getMessage());

            return "redirect:/customer";
        }
    }







}
