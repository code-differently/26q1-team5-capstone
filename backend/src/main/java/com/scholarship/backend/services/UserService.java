package com.scholarship.backend.services;

import com.scholarship.backend.entities.User;
import com.scholarship.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User createUser(String username, String password) {
        // Check if user already exists
        if (userRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        // Hash the password
        String hashedPassword = passwordEncoder.encode(password);

        // Create new user
        User user = new User(username, hashedPassword, "STUDENT");
        return userRepository.save(user);
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPasswordHash())) {
            return user;
        }
        return null; // Authentication failed
    }

    public User getUserById(long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }
}
