package com.scholarship.backend.services;

import com.scholarship.backend.entities.User;
import com.scholarship.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String username, String password) {
        // Check if user already exists
        if (userRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        User user = new User(username, password, "STUDENT");

        return userRepository.save(user);
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user != null && user.getPasswordHash().equals(password)) {
            return user;
        }

        return null;
    }

    public User getUserById(long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }
}