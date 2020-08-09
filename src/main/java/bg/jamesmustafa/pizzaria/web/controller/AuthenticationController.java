package bg.jamesmustafa.pizzaria.web.controller;

import bg.jamesmustafa.pizzaria.data.models.binding.UserAddBindingModel;
import bg.jamesmustafa.pizzaria.data.models.service.UserServiceModel;
import bg.jamesmustafa.pizzaria.data.models.view.UserDetailsViewModel;
import bg.jamesmustafa.pizzaria.service.UserDetailsServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class AuthenticationController {

    private final UserDetailsServiceImpl userService;
    private final ModelMapper modelMapper;

    public AuthenticationController(UserDetailsServiceImpl userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/login")
    public String login(){
        return "authenticate/login";
    }

    @GetMapping("/register")
    public String showRegisterView(Model model) {
        model.addAttribute("userRegisterForm", new UserAddBindingModel());
        return "authenticate/registration";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userRegisterForm") UserAddBindingModel user,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "registration/registration";
        }

        if (userService.isUsernameAvailable(user.getUsername())) {
            bindingResult.rejectValue("email",
                    "error.email",
                    "An account with this email already exists.");
            return "registration/registration";
        }

        userService.createAndLoginUser(user);

        return "redirect:/home";
    }

    @GetMapping("/authentication/profile")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String showProfile(Principal principal, Model model){

        UserDetailsViewModel user = this.modelMapper.map(this.userService.findUserByUsername(principal.getName()),UserDetailsViewModel.class);
        model.addAttribute("userProfile", user);

        return "authenticate/profile";
    }

    @GetMapping("authentication/edit")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String editUser(Principal principal, Model model) {
        //TODO: Is good practice to use principal, because if I pass id's everywhere its super easy to change the id
        //TODO: and see data of other users. Should look for a solution for this with Spring Secuirty as well...
        UserServiceModel user = this.modelMapper.map(this.userService.findUserByUsername(principal.getName()),UserServiceModel.class);

        model.addAttribute("userEditForm", user);

        return "authenticate/edit";
    }

    @PostMapping("authentication/edit/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String editUserConfirm(@Valid @ModelAttribute("userEditForm") UserServiceModel userModel,
                                     @PathVariable("id") Long userId,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes){

        if (bindingResult.hasErrors()) {
            return "redirect:/home";
        }
        this.userService.editUser(userId, userModel);
        return "redirect:/home";
    }

}
