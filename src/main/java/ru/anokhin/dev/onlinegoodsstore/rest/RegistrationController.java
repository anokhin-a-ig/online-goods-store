package ru.anokhin.dev.onlinegoodsstore.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anokhin.dev.onlinegoodsstore.dao.RegistrationDao;
import ru.anokhin.dev.onlinegoodsstore.entity.User;
import ru.anokhin.dev.onlinegoodsstore.service.UserService;

@RestController
@RequestMapping("/v1/registration")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> saveNewUser(@RequestBody RegistrationDao dao) {
        try {
            String username = userService.saveNewUser(dao);
            return ResponseEntity.status(HttpStatus.CREATED).body(username);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating user: " + e.getMessage());
        }
    }
}
