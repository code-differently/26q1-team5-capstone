package com.scholarship.backend.repositories;

import com.scholarship.backend.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername() {
        // Create and save a user
        User user = new User("testuser", "password", "STUDENT");
        User saved = userRepository.save(user);

        // Test finding by username
        User found = userRepository.findByUsername("testuser");
        assertNotNull(found);
        assertEquals("testuser", found.getUsername());
        assertEquals("STUDENT", found.getRole());
    }

    @Test
    void testFindByUsernameNotFound() {
        User found = userRepository.findByUsername("nonexistent");
        assertNull(found);
    }

    @Test
    void testSaveAndFindById() {
        User user = new User("saveuser", "password", "ADMIN");
        User saved = userRepository.save(user);

        assertNotNull(saved.getUserId());

        User found = userRepository.findById(saved.getUserId()).orElse(null);
        assertNotNull(found);
        assertEquals("saveuser", found.getUsername());
    }

    @Test
    void testFindAll() {
        User user1 = new User("user1", "pass1", "STUDENT");
        User user2 = new User("user2", "pass2", "ADMIN");
        userRepository.save(user1);
        userRepository.save(user2);

        Iterable<User> users = userRepository.findAll();
        assertNotNull(users);

        int count = 0;
        for (User u : users) {
            count++;
        }
        assertTrue(count >= 2);
    }
}