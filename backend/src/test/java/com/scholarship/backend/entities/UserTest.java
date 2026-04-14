package com.scholarship.backend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testuser", "hashedpassword123", "STUDENT");
    }

    @Test
    void testUserCreation() {
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("hashedpassword123", user.getPasswordHash());
        assertEquals("STUDENT", user.getRole());
    }

    @Test
    void testUserConstructorWithoutArgs() {
        User newUser = new User();
        assertNotNull(newUser);
    }

    @Test
    void testUserConstructorWithArgs() {
        User newUser = new User("admin", "adminpass", "ADMIN");
        assertEquals("admin", newUser.getUsername());
        assertEquals("adminpass", newUser.getPasswordHash());
        assertEquals("ADMIN", newUser.getRole());
    }

    @Test
    void testSetUsername() {
        user.setUsername("newusername");
        assertEquals("newusername", user.getUsername());
    }

    @Test
    void testSetPassword() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPasswordHash());
    }

    @Test
    void testSetPasswordHash() {
        user.setPasswordHash("hashednewpassword");
        assertEquals("hashednewpassword", user.getPasswordHash());
    }

    @Test
    void testSetRole() {
        user.setRole("ADMIN");
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    void testUserPersistence() {
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
    }

    @Test
    void testUserApplicationsCollection() {
        assertTrue(user.getApplications().isEmpty());
        assertNotNull(user.getApplications());
    }

    @Test
    void testSetApplications() {
        ArrayList<Application> apps = new ArrayList<>();
        user.setApplications(apps);
        assertNotNull(user.getApplications());
        assertTrue(user.getApplications().isEmpty());
    }

    @Test
    void testGetUserId() {
        assertTrue(user.getUserId() >= 0);
    }

    @Test
    void testGetUsername() {
        assertEquals("testuser", user.getUsername());
    }

    @Test
    void testGetRole() {
        assertEquals("STUDENT", user.getRole());
    }
}



