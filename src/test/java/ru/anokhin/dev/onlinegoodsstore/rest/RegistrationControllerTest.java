package ru.anokhin.dev.onlinegoodsstore.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.anokhin.dev.onlinegoodsstore.controllers.rest.RegistrationController;
import ru.anokhin.dev.onlinegoodsstore.dao.RegistrationDao;
import ru.anokhin.dev.onlinegoodsstore.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RegistrationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private RegistrationController registrationController;

    private ObjectMapper objectMapper;
    private RegistrationDao registrationDao;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
        objectMapper = new ObjectMapper();
        registrationDao = new RegistrationDao("testUser", "testPassword");
    }

    @Test
    @DisplayName("Should successfully create new user")
    void saveNewUser_Success() throws Exception {
        String exceptedUsername = "testUser";
        when(userService.saveNewUser(any(RegistrationDao.class))).thenReturn(exceptedUsername);

        mockMvc.perform(post("/v1/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDao)))
                .andExpect(status().isCreated())
                .andExpect(content().string(exceptedUsername));

        verify(userService, times(1)).saveNewUser(any(RegistrationDao.class));
    }
}
