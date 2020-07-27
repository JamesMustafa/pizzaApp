package bg.jamesmustafa.pizzaria.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class HomeController {
    @GetMapping("/home")
    public String home(){
        return "/home";
    }

}
