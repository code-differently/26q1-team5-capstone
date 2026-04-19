package com.scholarship.backend.services;

import com.scholarship.backend.entities.User;
import com.scholarship.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("jayden", "password123", "STUDENT");
    }

    // --- createUser ---

    @Test
    void createUser_Success() {
        when(userRepository.findByUsername("jayden")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.createUser("jayden", "password123");

        assertNotNull(result);
        assertEquals("jayden", result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_ThrowsWhenUsernameAlreadyExists() {
        when(userRepository.findByUsername("jayden")).thenReturn(testUser);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> userService.createUser("jayden", "password123")
        );

        assertTrue(ex.getMessage().contains("Username already exists"));
        verify(userRepository, never()).save(any(User.class));
    }

    // --- authenticate ---

    @Test
    void authenticate_Success() {
        when(userRepository.findByUsername("jayden")).thenReturn(testUser);

        User result = userService.authenticate("jayden", "password123");

        assertNotNull(result);
        assertEquals("jayden", result.getUsername());
    }

    @Test
    void authenticate_ReturnsNullWhenUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(null);

        User result = userService.authenticate("unknown", "password123");

        assertNull(result);
    }

    @Test
    void authenticate_ReturnsNullWhenPasswordWrong() {
        when(userRepository.findByUsername("jayden")).thenReturn(testUser);

        User result = userService.authenticate("jayden", "wrongpassword");

        assertNull(result);
    }

    // --- getUserById ---

    @Test
    void getUserById_ReturnsUserWhenFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("jayden", result.getUsername());
    }

    @Test
    void getUserById_ReturnsNullWhenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        User result = userService.getUserById(99L);

        assertNull(result);
    }

    // --- deleteUser ---

    @Test
    void deleteUser_CallsRepositoryDelete() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}