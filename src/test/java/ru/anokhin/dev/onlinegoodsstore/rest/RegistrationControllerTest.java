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
import ru.anokhin.dev.onlinegoodsstore.exception.GlobalExceptionHandler;
import ru.anokhin.dev.onlinegoodsstore.exception.UserAlreadyExistsException;
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
    private RegistrationDao validDao;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(registrationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        validDao = new RegistrationDao("testUser", "testPassword");
    }

    @Test
    @DisplayName("Should return 201 Created when user is successfully created")
    void saveNewUser_Success() throws Exception {
        String expectedUsername = "testUser";
        when(userService.saveNewUser(any(RegistrationDao.class))).thenReturn(expectedUsername);

        mockMvc.perform(post("/v1/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDao)))
                .andExpect(status().isCreated())
                .andExpect(content().string(expectedUsername));

        verify(userService, times(1)).saveNewUser(any(RegistrationDao.class));
    }

    @Test
    @DisplayName("Should return 409 Conflict when username already exists")
    void saveNewUser_DuplicateUser_ReturnsConflict() throws Exception {
        when(userService.saveNewUser(any(RegistrationDao.class)))
                .thenThrow(new UserAlreadyExistsException("User with username 'testUser' already exists"));

        mockMvc.perform(post("/v1/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDao)))
                .andExpect(status().isConflict())
                .andExpect(content().string("User with username 'testUser' already exists"));

        verify(userService, times(1)).saveNewUser(any(RegistrationDao.class));
    }

    @Test
    @DisplayName("Should return 500 Internal Server Error on unexpected exception")
    void saveNewUser_UnexpectedError_Returns500() throws Exception {
        when(userService.saveNewUser(any(RegistrationDao.class)))
                .thenThrow(new RuntimeException("Unexpected database error"));

        mockMvc.perform(post("/v1/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDao)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal server error"));

        verify(userService, times(1)).saveNewUser(any(RegistrationDao.class));
    }
}
