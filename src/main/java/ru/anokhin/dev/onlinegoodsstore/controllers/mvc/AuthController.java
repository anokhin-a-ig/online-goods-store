package ru.anokhin.dev.onlinegoodsstore.controllers.mvc;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.anokhin.dev.onlinegoodsstore.dao.RegistrationRequest;
import ru.anokhin.dev.onlinegoodsstore.service.security.AuthenticationService;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest("", "", "", ""));
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegistrationRequest request,
                           BindingResult result) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        authenticationService.register(
                request.email(),
                request.password(),
                request.name(),
                request.deliveryAddress()
        );
        return "redirect:/auth/login?registered";
    }
}
