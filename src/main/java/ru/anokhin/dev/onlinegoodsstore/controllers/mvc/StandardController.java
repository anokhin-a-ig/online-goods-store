package ru.anokhin.dev.onlinegoodsstore.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class StandardController {

    @GetMapping("/index")
    public String indexPAge() {
        System.out.println("123");
        return "index";
    }

    @GetMapping("/")
    public String redirectToIndex() {
        System.out.println("redirect to index.html");
        return "redirect:/index.html";
    }

    @GetMapping("/login")
    public String loginPage() {
        System.out.println("redirect to index.html");
        return "/login";
    }
}
