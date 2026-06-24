package ru.anokhin.dev.onlinegoodsstore.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.anokhin.dev.onlinegoodsstore.dao.RegistrationDao;
import ru.anokhin.dev.onlinegoodsstore.entity.Role;
import ru.anokhin.dev.onlinegoodsstore.entity.User;
import ru.anokhin.dev.onlinegoodsstore.exception.UserAlreadyExistsException;
import ru.anokhin.dev.onlinegoodsstore.repository.RoleRepository;
import ru.anokhin.dev.onlinegoodsstore.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    private final RegistrationDao validDao = new RegistrationDao("testUser", "testPassword");
    private final Role defaultRole = Role.builder().id(1L).roleName("ROLE_USER").build();

    @Test
    @DisplayName("Should create user successfully with default role")
    void saveNewUser_Success() {
        when(userRepository.findUserByUsername("testUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("testPassword")).thenReturn("encodedPassword");
        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(Optional.of(defaultRole));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String result = userService.saveNewUser(validDao);

        assertEquals("testUser", result);
        verify(userRepository).findUserByUsername("testUser");
        verify(passwordEncoder).encode("testPassword");
        verify(roleRepository).findByRoleName("ROLE_USER");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when username is taken")
    void saveNewUser_DuplicateUsername_ThrowsException() {
        when(userRepository.findUserByUsername("testUser")).thenReturn(Optional.of(new User()));

        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.saveNewUser(validDao)
        );

        assertEquals("User with username 'testUser' already exists", exception.getMessage());
        verify(userRepository).findUserByUsername("testUser");
        verifyNoInteractions(passwordEncoder, roleRepository);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw RuntimeException when default role is not found")
    void saveNewUser_RoleNotFound_ThrowsException() {
        when(userRepository.findUserByUsername("testUser")).thenReturn(Optional.empty());
        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.saveNewUser(validDao)
        );

        assertEquals("Default role not found", exception.getMessage());
        verify(userRepository).findUserByUsername("testUser");
        verify(roleRepository).findByRoleName("ROLE_USER");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should trim username before saving")
    void saveNewUser_TrimsUsername() {
        RegistrationDao daoWithSpaces = new RegistrationDao("  spacedUser  ", "password123");
        when(userRepository.findUserByUsername("  spacedUser  ")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(Optional.of(defaultRole));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String result = userService.saveNewUser(daoWithSpaces);

        assertEquals("spacedUser", result);
        verify(userRepository).save(argThat(user -> user.getUsername().equals("spacedUser")));
    }
}
