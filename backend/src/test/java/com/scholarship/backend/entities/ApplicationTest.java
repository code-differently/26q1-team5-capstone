package com.scholarship.backend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    private User user;
    private Scholarship scholarship;
    private Application application;

    @BeforeEach
    void setUp() {
        user = new User("testuser", "password", "STUDENT");
        LocalDate deadline = LocalDate.of(2026, 6, 30);
        scholarship = new Scholarship("Merit Award", 5000.0, deadline);
        application = new Application(user, scholarship);
    }

    @Test
    void testApplicationCreation() {
        assertNotNull(application);
        assertEquals(user, application.getUser());
        assertEquals(scholarship, application.getScholarship());
        assertEquals(ApplicationStatus.SAVED, application.getStatus());
        assertEquals(LocalDate.now(), application.getSavedDate());
    }

    @Test
    void testApplicationConstructorWithoutArgs() {
        Application newApp = new Application();
        assertNotNull(newApp);
    }

    @Test
    void testSetStatus() {
        application.setStatus(ApplicationStatus.IN_PROGRESS);
        assertEquals(ApplicationStatus.IN_PROGRESS, application.getStatus());

        application.setStatus(ApplicationStatus.SUBMITTED);
        assertEquals(ApplicationStatus.SUBMITTED, application.getStatus());
    }

    @Test
    void testSetSubmittedDate() {
        LocalDate submittedDate = LocalDate.of(2026, 5, 15);
        application.setSubmittedDate(submittedDate);
        assertEquals(submittedDate, application.getSubmittedDate());
    }

    @Test
    void testSetDeadlineAlert() {
        LocalDate alertDate = LocalDate.of(2026, 6, 20);
        application.setDeadlineAlert(alertDate);
        assertEquals(alertDate, application.getDeadlineAlert());
    }

    @Test
    void testSetNotes() {
        String notes = "Strong candidate, highlighted achievements";
        application.setNotes(notes);
        assertEquals(notes, application.getNotes());
    }

    @Test
    void testApplicationStatusTransitions() {
        application.setStatus(ApplicationStatus.SAVED);
        assertEquals(ApplicationStatus.SAVED, application.getStatus());

        application.setStatus(ApplicationStatus.IN_PROGRESS);
        assertEquals(ApplicationStatus.IN_PROGRESS, application.getStatus());

        application.setStatus(ApplicationStatus.SUBMITTED);
        assertEquals(ApplicationStatus.SUBMITTED, application.getStatus());

        application.setStatus(ApplicationStatus.AWARDED);
        assertEquals(ApplicationStatus.AWARDED, application.getStatus());
    }

    @Test
    void testApplicationRejection() {
        application.setStatus(ApplicationStatus.REJECTED);
        assertEquals(ApplicationStatus.REJECTED, application.getStatus());
    }

    @Test
    void testApplicationWithAllFields() {
        LocalDate submittedDate = LocalDate.of(2026, 5, 10);
        LocalDate alertDate = LocalDate.of(2026, 6, 25);
        String notes = "Perfect match for this scholarship";

        application.setSubmittedDate(submittedDate);
        application.setDeadlineAlert(alertDate);
        application.setNotes(notes);
        application.setStatus(ApplicationStatus.SUBMITTED);

        assertEquals(ApplicationStatus.SUBMITTED, application.getStatus());
        assertEquals(submittedDate, application.getSubmittedDate());
        assertEquals(alertDate, application.getDeadlineAlert());
        assertEquals(notes, application.getNotes());
    }

    @Test
    void testApplicationUserRelationship() {
        assertEquals(user, application.getUser());
        assertEquals("testuser", application.getUser().getUsername());
    }

    @Test
    void testApplicationScholarshipRelationship() {
        assertEquals(scholarship, application.getScholarship());
        assertEquals("Merit Award", application.getScholarship().getName());
    }

    @Test
    void testGetApplicationId() {
        assertTrue(application.getApplicationId() >= 0);
    }

    @Test
    void testSetUser() {
        User newUser = new User("newuser", "pass", "ADMIN");
        application.setUser(newUser);
        assertEquals(newUser, application.getUser());
    }

    @Test
    void testSetScholarship() {
        LocalDate newDeadline = LocalDate.of(2026, 7, 31);
        Scholarship newScholarship = new Scholarship("Different Scholarship", 7500.0, newDeadline);
        application.setScholarship(newScholarship);
        assertEquals(newScholarship, application.getScholarship());
    }
}