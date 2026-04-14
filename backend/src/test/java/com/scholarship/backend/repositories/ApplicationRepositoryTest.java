package com.scholarship.backend.repositories;

import com.scholarship.backend.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ApplicationRepositoryIntegrationTest {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScholarshipRepository scholarshipRepository;

    @Test
    void testFindByUser_UserId() {
        // Create users and scholarships
        User user1 = new User("user1", "pass1", "STUDENT");
        User user2 = new User("user2", "pass2", "STUDENT");
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        Scholarship scholarship1 = new Scholarship("Scholarship 1", 5000.0, LocalDate.of(2026, 6, 30));
        Scholarship scholarship2 = new Scholarship("Scholarship 2", 3000.0, LocalDate.of(2026, 7, 15));
        scholarship1 = scholarshipRepository.save(scholarship1);
        scholarship2 = scholarshipRepository.save(scholarship2);

        // Create applications
        Application app1 = new Application(user1, scholarship1);
        Application app2 = new Application(user1, scholarship2);
        Application app3 = new Application(user2, scholarship1);
        applicationRepository.save(app1);
        applicationRepository.save(app2);
        applicationRepository.save(app3);

        // Test finding by user ID
        List<Application> user1Apps = applicationRepository.findByUser_UserId(user1.getUserId());
        assertEquals(2, user1Apps.size());

        List<Application> user2Apps = applicationRepository.findByUser_UserId(user2.getUserId());
        assertEquals(1, user2Apps.size());
    }

    @Test
    void testFindByStatus() {
        // Create users and scholarships
        User user = new User("testuser", "password", "STUDENT");
        user = userRepository.save(user);

        Scholarship scholarship1 = new Scholarship("Scholarship 1", 5000.0, LocalDate.of(2026, 6, 30));
        Scholarship scholarship2 = new Scholarship("Scholarship 2", 3000.0, LocalDate.of(2026, 7, 15));
        scholarship1 = scholarshipRepository.save(scholarship1);
        scholarship2 = scholarshipRepository.save(scholarship2);

        // Create applications with different statuses
        Application savedApp = new Application(user, scholarship1);
        savedApp.setStatus(ApplicationStatus.SAVED);

        Application submittedApp = new Application(user, scholarship2);
        submittedApp.setStatus(ApplicationStatus.SUBMITTED);
        submittedApp.setSubmittedDate(LocalDate.now());

        applicationRepository.save(savedApp);
        applicationRepository.save(submittedApp);

        // Test finding by status
        List<Application> savedApps = applicationRepository.findByStatus(ApplicationStatus.SAVED);
        assertEquals(1, savedApps.size());
        assertEquals(ApplicationStatus.SAVED, savedApps.get(0).getStatus());

        List<Application> submittedApps = applicationRepository.findByStatus(ApplicationStatus.SUBMITTED);
        assertEquals(1, submittedApps.size());
        assertEquals(ApplicationStatus.SUBMITTED, submittedApps.get(0).getStatus());

        List<Application> inProgressApps = applicationRepository.findByStatus(ApplicationStatus.IN_PROGRESS);
        assertTrue(inProgressApps.isEmpty());
    }

    @Test
    void testFindByUser_UserIdAndStatus() {
        // Create users
        User user1 = new User("user1", "pass1", "STUDENT");
        User user2 = new User("user2", "pass2", "STUDENT");
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        // Create scholarships
        Scholarship scholarship1 = new Scholarship("Scholarship 1", 5000.0, LocalDate.of(2026, 6, 30));
        Scholarship scholarship2 = new Scholarship("Scholarship 2", 3000.0, LocalDate.of(2026, 7, 15));
        scholarship1 = scholarshipRepository.save(scholarship1);
        scholarship2 = scholarshipRepository.save(scholarship2);

        // Create applications with different statuses for different users
        Application user1Saved = new Application(user1, scholarship1);
        user1Saved.setStatus(ApplicationStatus.SAVED);

        Application user1Submitted = new Application(user1, scholarship2);
        user1Submitted.setStatus(ApplicationStatus.SUBMITTED);

        Application user2Saved = new Application(user2, scholarship1);
        user2Saved.setStatus(ApplicationStatus.SAVED);

        applicationRepository.save(user1Saved);
        applicationRepository.save(user1Submitted);
        applicationRepository.save(user2Saved);

        // Test finding by user ID and status
        List<Application> user1SavedApps = applicationRepository.findByUser_UserIdAndStatus(user1.getUserId(), ApplicationStatus.SAVED);
        assertEquals(1, user1SavedApps.size());

        List<Application> user1SubmittedApps = applicationRepository.findByUser_UserIdAndStatus(user1.getUserId(), ApplicationStatus.SUBMITTED);
        assertEquals(1, user1SubmittedApps.size());

        List<Application> user2SavedApps = applicationRepository.findByUser_UserIdAndStatus(user2.getUserId(), ApplicationStatus.SAVED);
        assertEquals(1, user2SavedApps.size());

        List<Application> user2SubmittedApps = applicationRepository.findByUser_UserIdAndStatus(user2.getUserId(), ApplicationStatus.SUBMITTED);
        assertTrue(user2SubmittedApps.isEmpty());
    }

    @Test
    void testSaveAndFindById() {
        User user = new User("testuser", "password", "STUDENT");
        user = userRepository.save(user);

        Scholarship scholarship = new Scholarship("Test Scholarship", 2500.0, LocalDate.of(2026, 9, 1));
        scholarship = scholarshipRepository.save(scholarship);

        Application application = new Application(user, scholarship);
        application.setStatus(ApplicationStatus.IN_PROGRESS);
        application.setNotes("Test application notes");
        application = applicationRepository.save(application);

        assertNotNull(application.getApplicationId());

        Application found = applicationRepository.findById(application.getApplicationId()).orElse(null);
        assertNotNull(found);
        assertEquals(ApplicationStatus.IN_PROGRESS, found.getStatus());
        assertEquals("Test application notes", found.getNotes());
    }

    @Test
    void testFindByUser_UserIdEmpty() {
        List<Application> applications = applicationRepository.findByUser_UserId(999L);
        assertTrue(applications.isEmpty());
    }

    @Test
    void testFindByStatusEmpty() {
        List<Application> applications = applicationRepository.findByStatus(ApplicationStatus.AWARDED);
        assertTrue(applications.isEmpty());
    }

    @Test
    void testFindByUser_UserIdAndStatusEmpty() {
        List<Application> applications = applicationRepository.findByUser_UserIdAndStatus(999L, ApplicationStatus.SAVED);
        assertTrue(applications.isEmpty());
    }
}
