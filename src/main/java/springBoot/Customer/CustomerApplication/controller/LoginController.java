package springBoot.Customer.CustomerApplication.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springBoot.Customer.CustomerApplication.entity.Customer;
import springBoot.Customer.CustomerApplication.entity.Login;
import springBoot.Customer.CustomerApplication.response.LoginResponse;
import springBoot.Customer.CustomerApplication.service.AuthenticationService;
import springBoot.Customer.CustomerApplication.service.CustomerService;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationService authenticationService;
    private final CustomerService customerService;

    @PostMapping("/signin")
    public String signin(@ModelAttribute Login signinRequest, HttpServletRequest request,Model m) {

        try {
            String token = authenticationService.signin(signinRequest);
            HttpSession session = request.getSession();
            session.setAttribute("token", token);
            return "redirect:/customer";
        } catch (IllegalArgumentException e) {
            m.addAttribute("error", e.getMessage());
            return "login"; // return to login page with error message
        } catch (Exception e) {
            m.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/loginpage")
    public String loginview(){

        return "login";
    }


}
