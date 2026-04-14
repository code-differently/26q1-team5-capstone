package com.scholarship.backend.repositories;

import com.scholarship.backend.entities.Profile;
import com.scholarship.backend.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProfileRepositoryIntegrationTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUser_UserId() {
        // Create and save user and profile
        User user = new User("testuser", "password", "STUDENT");
        user = userRepository.save(user);

        Profile profile = new Profile(user, "John Doe");
        profile.setGpa(3.8);
        profile.setMajor("Computer Science");
        profile = profileRepository.save(profile);

        // Test finding by user ID
        Profile found = profileRepository.findByUser_UserId(user.getUserId());
        assertNotNull(found);
        assertEquals("John Doe", found.getName());
        assertEquals(3.8, found.getGpa());
        assertEquals("Computer Science", found.getMajor());
        assertEquals(user.getUserId(), found.getUser().getUserId());
    }

    @Test
    void testFindByUser_UserIdNotFound() {
        Profile found = profileRepository.findByUser_UserId(999L);
        assertNull(found);
    }

    @Test
    void testDeleteByUser_UserId() {
        // Create and save user and profile
        User user = new User("deleteuser", "password", "STUDENT");
        user = userRepository.save(user);

        Profile profile = new Profile(user, "Delete Me");
        profile = profileRepository.save(profile);

        // Verify profile exists
        Profile found = profileRepository.findByUser_UserId(user.getUserId());
        assertNotNull(found);

        // Delete by user ID
        profileRepository.deleteByUser_UserId(user.getUserId());

        // Verify profile is deleted
        Profile deleted = profileRepository.findByUser_UserId(user.getUserId());
        assertNull(deleted);
    }

    @Test
    void testSaveAndFindById() {
        User user = new User("saveuser", "password", "STUDENT");
        user = userRepository.save(user);

        Profile profile = new Profile(user, "Save Test");
        profile.setState("California");
        profile.setNeedsFinancialAid(true);
        profile = profileRepository.save(profile);

        assertNotNull(profile.getProfileId());

        Profile found = profileRepository.findById(profile.getProfileId()).orElse(null);
        assertNotNull(found);
        assertEquals("Save Test", found.getName());
        assertEquals("California", found.getState());
        assertTrue(found.getNeedsFinancialAid());
    }
}
