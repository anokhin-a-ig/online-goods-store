package ru.anokhin.dev.onlinegoodsstore.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Should return 409 Conflict for UserAlreadyExistsException")
    void handleUserAlreadyExists_ReturnsConflict() {
        UserAlreadyExistsException exception =
                new UserAlreadyExistsException("User with username 'john' already exists");

        ResponseEntity<String> response = handler.handleUserAlreadyExists(exception);

        assertEquals(409, response.getStatusCode().value());
        assertEquals("User with username 'john' already exists", response.getBody());
    }

    @Test
    @DisplayName("Should return 400 Bad Request for validation errors")
    void handleValidation_ReturnsBadRequest() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("dao", "username", "Username is required"),
                new FieldError("dao", "password", "Password must be at least 6 characters")
        ));
        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<String> response = handler.handleValidation(exception);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Username is required, Password must be at least 6 characters", response.getBody());
    }

    @Test
    @DisplayName("Should return 500 Internal Server Error for generic exceptions")
    void handleGeneral_ReturnsInternalServerError() {
        Exception exception = new RuntimeException("Some internal error");

        ResponseEntity<String> response = handler.handleGeneral(exception);

        assertEquals(500, response.getStatusCode().value());
        assertEquals("Internal server error", response.getBody());
    }

    @Test
    @DisplayName("Should not expose internal error details")
    void handleGeneral_DoesNotExposeDetails() {
        Exception exception = new RuntimeException("Database connection failed: timeout on pool");

        ResponseEntity<String> response = handler.handleGeneral(exception);

        assertEquals(500, response.getStatusCode().value());
        assertEquals("Internal server error", response.getBody());
        assertEquals(Objects.requireNonNull(response.getBody()).contains("Database"), false);
    }
}
