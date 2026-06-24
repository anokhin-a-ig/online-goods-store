package ru.anokhin.dev.onlinegoodsstore.dao;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationDaoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Should pass validation for valid data")
    void validDao_PassesValidation() {
        RegistrationDao dao = new RegistrationDao("validUser", "validPass123");

        Set<ConstraintViolation<RegistrationDao>> violations = validator.validate(dao);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should reject blank username")
    void blankUsername_Rejected() {
        RegistrationDao dao = new RegistrationDao("", "password123");

        Set<ConstraintViolation<RegistrationDao>> violations = validator.validate(dao);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Username is required")));
    }

    @Test
    @DisplayName("Should reject null username")
    void nullUsername_Rejected() {
        RegistrationDao dao = new RegistrationDao(null, "password123");

        Set<ConstraintViolation<RegistrationDao>> violations = validator.validate(dao);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Username is required")));
    }

    @Test
    @DisplayName("Should reject username shorter than 3 characters")
    void shortUsername_Rejected() {
        RegistrationDao dao = new RegistrationDao("ab", "password123");

        Set<ConstraintViolation<RegistrationDao>> violations = validator.validate(dao);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("between 3 and 50")));
    }

    @Test
    @DisplayName("Should reject username longer than 50 characters")
    void longUsername_Rejected() {
        String longUsername = "a".repeat(51);
        RegistrationDao dao = new RegistrationDao(longUsername, "password123");

        Set<ConstraintViolation<RegistrationDao>> violations = validator.validate(dao);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("between 3 and 50")));
    }

    @Test
    @DisplayName("Should reject blank password")
    void blankPassword_Rejected() {
        RegistrationDao dao = new RegistrationDao("validUser", "");

        Set<ConstraintViolation<RegistrationDao>> violations = validator.validate(dao);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Password is required")));
    }

    @Test
    @DisplayName("Should reject null password")
    void nullPassword_Rejected() {
        RegistrationDao dao = new RegistrationDao("validUser", null);

        Set<ConstraintViolation<RegistrationDao>> violations = validator.validate(dao);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Password is required")));
    }

    @Test
    @DisplayName("Should reject password shorter than 6 characters")
    void shortPassword_Rejected() {
        RegistrationDao dao = new RegistrationDao("validUser", "12345");

        Set<ConstraintViolation<RegistrationDao>> violations = validator.validate(dao);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("at least 6")));
    }
}
