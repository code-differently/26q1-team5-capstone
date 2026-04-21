package com.scholarship.backend.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {

    private User user;
    private Profile profile;

    @BeforeEach
    void setUp() {
        user = new User("testuser", "password", "STUDENT");
        profile = new Profile(user, "John Doe");
    }

    @Test
    void testProfileCreation() {
        assertNotNull(profile);
        assertEquals("John Doe", profile.getName());
        assertEquals(user, profile.getUser());
    }

    @Test
    void testProfileConstructorWithoutArgs() {
        Profile newProfile = new Profile();
        assertNotNull(newProfile);
    }

    @Test
    void testProfileConstructorWithArgs() {
        Profile newProfile = new Profile(user, "Jane Smith");
        assertEquals("Jane Smith", newProfile.getName());
        assertEquals(user, newProfile.getUser());
    }

    @Test
    void testSetName() {
        profile.setName("Jane Doe");
        assertEquals("Jane Doe", profile.getName());
    }

    @Test
    void testSetGpa() {
        profile.setGpa(3.85);
        assertEquals(3.85, profile.getGpa());
    }

    @Test
    void testSetMajor() {
        profile.setMajor("Computer Science");
        assertEquals("Computer Science", profile.getMajor());
    }

    @Test
    void testSetEnrollmentStatus() {
        profile.setEnrollmentStatus("FULL_TIME");
        assertEquals("FULL_TIME", profile.getEnrollmentStatus());
    }

    @Test
    void testSetState() {
        profile.setState("California");
        assertEquals("California", profile.getState());
    }

    @Test
    void testSetNeedsFinancialAid() {
        profile.setNeedsFinancialAid(true);
        assertTrue(profile.getNeedsFinancialAid());

        profile.setNeedsFinancialAid(false);
        assertFalse(profile.getNeedsFinancialAid());
    }

    @Test
    void testSetEthnicity() {
        profile.setEthnicity("Asian");
        assertEquals("Asian", profile.getEthnicity());
    }

    @Test
    void testSetCareerGoals() {
        String goals = "Become a software engineer";
        profile.setCareerGoals(goals);
        assertEquals(goals, profile.getCareerGoals());
    }

    @Test
    void testSetInterests() {
        String interests = "AI, Machine Learning, Web Development";
        profile.setInterests(interests);
        assertEquals(interests, profile.getInterests());
    }

    @Test
    void testAllProfileFields() {
        profile.setGpa(3.75);
        profile.setMajor("Engineering");
        profile.setEnrollmentStatus("FULL_TIME");
        profile.setNeedsFinancialAid(true);
        profile.setState("Texas");
        profile.setEthnicity("Hispanic");
        profile.setCareerGoals("Build innovative products");
        profile.setInterests("Cloud Computing, DevOps");

        assertEquals(3.75, profile.getGpa());
        assertEquals("Engineering", profile.getMajor());
        assertEquals("FULL_TIME", profile.getEnrollmentStatus());
        assertTrue(profile.getNeedsFinancialAid());
        assertEquals("Texas", profile.getState());
        assertEquals("Hispanic", profile.getEthnicity());
        assertEquals("Build innovative products", profile.getCareerGoals());
        assertEquals("Cloud Computing, DevOps", profile.getInterests());
    }

    @Test
    void testGetProfileId() {
        assertTrue(profile.getProfileId() >= 0);
    }

    @Test
    void testGetUser() {
        assertEquals(user, profile.getUser());
    }
}