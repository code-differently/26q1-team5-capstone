package com.scholarship.backend.controllers;

import com.scholarship.backend.entities.User;
import com.scholarship.backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User mockUser;

    @BeforeEach
    void setUp() throws Exception {
        mockUser = new User("jayden", "secret123", "STUDENT");
        java.lang.reflect.Field idField = User.class.getDeclaredField("userId");
        idField.setAccessible(true);
        idField.set(mockUser, 1L);
    }

    // -------------------------------------------------------------------------
    // POST /api/users/register
    // -------------------------------------------------------------------------

    @Test
    void register_returnsCreatedUser() {
        when(userService.createUser("jayden", "secret123")).thenReturn(mockUser);

        ResponseEntity<User> response = userController.register(
                Map.of("username", "jayden", "password", "secret123"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jayden", response.getBody().getUsername());
        assertEquals("STUDENT", response.getBody().getRole());
    }

    @Test
    void register_whenUsernameTaken_throwsException() {
        when(userService.createUser("jayden", "secret123"))
                .thenThrow(new IllegalArgumentException("Username already exists: jayden"));

        assertThrows(IllegalArgumentException.class, () ->
                userController.register(
                        Map.of("username", "jayden", "password", "secret123")));
    }

    // -------------------------------------------------------------------------
    // POST /api/users/login
    // -------------------------------------------------------------------------

    @Test
    void login_withValidCredentials_returnsUser() {
        when(userService.authenticate("jayden", "secret123")).thenReturn(mockUser);

        ResponseEntity<User> response = userController.login(
                Map.of("username", "jayden", "password", "secret123"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jayden", response.getBody().getUsername());
    }

    @Test
    void login_withBadCredentials_returnsOkWithNullBody() {
        when(userService.authenticate("jayden", "wrongpass")).thenReturn(null);

        ResponseEntity<User> response = userController.login(
                Map.of("username", "jayden", "password", "wrongpass"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    // -------------------------------------------------------------------------
    // GET /api/users/{id}
    // -------------------------------------------------------------------------

    @Test
    void getUser_whenFound_returnsUser() {
        when(userService.getUserById(1L)).thenReturn(mockUser);

        ResponseEntity<User> response = userController.getUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jayden", response.getBody().getUsername());
        assertEquals("STUDENT", response.getBody().getRole());
    }

    @Test
    void getUser_whenNotFound_returnsOkWithNullBody() {
        when(userService.getUserById(99L)).thenReturn(null);

        ResponseEntity<User> response = userController.getUser(99L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    // -------------------------------------------------------------------------
    // DELETE /api/users/{id}
    // -------------------------------------------------------------------------

    @Test
    void deleteUser_returnsNoContent() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void deleteUser_nonExistentId_stillReturnsNoContent() {
        doNothing().when(userService).deleteUser(99L);

        ResponseEntity<Void> response = userController.deleteUser(99L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}