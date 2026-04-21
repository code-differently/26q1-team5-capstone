package com.scholarship.backend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ScholarshipTest {

    private Scholarship scholarship;

    @BeforeEach
    void setUp() {
        LocalDate deadline = LocalDate.of(2026, 6, 30);
        scholarship = new Scholarship("Merit Scholarship", 5000.0, deadline);
    }

    @Test
    void testScholarshipCreation() {
        assertNotNull(scholarship);
        assertEquals("Merit Scholarship", scholarship.getName());
        assertEquals(5000.0, scholarship.getAmount());
        assertEquals(LocalDate.of(2026, 6, 30), scholarship.getDeadline());
    }

    @Test
    void testScholarshipConstructorWithoutArgs() {
        Scholarship newScholarship = new Scholarship();
        assertNotNull(newScholarship);
    }

    @Test
    void testSetName() {
        scholarship.setName("Academic Excellence Award");
        assertEquals("Academic Excellence Award", scholarship.getName());
    }

    @Test
    void testSetDescription() {
        String description = "Award for academically excellent students";
        scholarship.setDescription(description);
        assertEquals(description, scholarship.getDescription());
    }

    @Test
    void testSetAmount() {
        scholarship.setAmount(10000.0);
        assertEquals(10000.0, scholarship.getAmount());
    }

    @Test
    void testSetDeadline() {
        LocalDate newDeadline = LocalDate.of(2026, 12, 31);
        scholarship.setDeadline(newDeadline);
        assertEquals(newDeadline, scholarship.getDeadline());
    }

    @Test
    void testSetEligibilityCriteria() {
        String criteria = "GPA >= 3.5, Full-time student";
        scholarship.setEligibilityCriteria(criteria);
        assertEquals(criteria, scholarship.getEligibilityCriteria());
    }

    @Test
    void testSetApplicationUrl() {
        String url = "https://example.com/apply";
        scholarship.setApplicationUrl(url);
        assertEquals(url, scholarship.getApplicationUrl());
    }

    @Test
    void testSetFieldOfStudy() {
        scholarship.setFieldOfStudy("Computer Science");
        assertEquals("Computer Science", scholarship.getFieldOfStudy());
    }

    @Test
    void testSetState() {
        scholarship.setState("California");
        assertEquals("California", scholarship.getState());
    }

    @Test
    void testSetSourceApi() {
        scholarship.setSourceApi("ExternalAPI");
        assertEquals("ExternalAPI", scholarship.getSourceApi());
    }

    @Test
    void testAllScholarshipFields() {
        LocalDate deadline = LocalDate.of(2026, 8, 15);
        scholarship = new Scholarship("Full Ride", 50000.0, deadline);
        scholarship.setDescription("Full tuition coverage");
        scholarship.setEligibilityCriteria("Top 1% students");
        scholarship.setApplicationUrl("https://fullride.edu");
        scholarship.setFieldOfStudy("Engineering");
        scholarship.setState("Texas");
        scholarship.setSourceApi("GoalApi");

        assertEquals("Full Ride", scholarship.getName());
        assertEquals(50000.0, scholarship.getAmount());
        assertEquals("Full tuition coverage", scholarship.getDescription());
        assertEquals("Top 1% students", scholarship.getEligibilityCriteria());
        assertEquals("https://fullride.edu", scholarship.getApplicationUrl());
        assertEquals("Engineering", scholarship.getFieldOfStudy());
        assertEquals("Texas", scholarship.getState());
        assertEquals("GoalApi", scholarship.getSourceApi());
    }

    @Test
    void testScholarshipApplicationsCollection() {
        assertNotNull(scholarship.getApplications());
        assertTrue(scholarship.getApplications().isEmpty());
    }

    @Test
    void testGetScholarshipId() {
        assertTrue(scholarship.getScholarshipId() >= 0);
    }
}