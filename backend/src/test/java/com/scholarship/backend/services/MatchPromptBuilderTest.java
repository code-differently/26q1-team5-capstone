package com.scholarship.backend.services;

import com.scholarship.backend.entities.Profile;
import com.scholarship.backend.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MatchPromptBuilderTest {

    @InjectMocks
    private MatchPromptBuilder matchPromptBuilder;

    private Profile fullProfile;
    private Profile emptyProfile;

    @BeforeEach
    void setUp() {
        User testUser = new User("jayden", "password123", "STUDENT");

        fullProfile = new Profile();
        fullProfile.setUser(testUser);
        fullProfile.setName("Jayden");
        fullProfile.setGpa(3.8);
        fullProfile.setMajor("Computer Science");
        fullProfile.setEnrollmentStatus("Full-time");
        fullProfile.setNeedsFinancialAid(true);
        fullProfile.setState("Delaware");
        fullProfile.setEthnicity("Black or African American");
        fullProfile.setCareerGoals("Software Engineer");
        fullProfile.setInterests("AI, robotics");

        emptyProfile = new Profile();
        emptyProfile.setUser(testUser);
    }

    @Test
    void buildPrompt_ReturnsNonEmptyString() {
        String prompt = matchPromptBuilder.buildPrompt(fullProfile);
        assertNotNull(prompt);
        assertFalse(prompt.isBlank());
    }

    @Test
    void buildPrompt_ContainsSystemInstruction() {
        String prompt = matchPromptBuilder.buildPrompt(fullProfile);
        assertTrue(prompt.contains("You are an AI scholarship recommendation assistant"));
    }

    @Test
    void buildPrompt_ContainsAllProfileFields() {
        String prompt = matchPromptBuilder.buildPrompt(fullProfile);

        assertTrue(prompt.contains("Jayden"));
        assertTrue(prompt.contains("3.8"));
        assertTrue(prompt.contains("Computer Science"));
        assertTrue(prompt.contains("Full-time"));
        assertTrue(prompt.contains("true"));
        assertTrue(prompt.contains("Delaware"));
        assertTrue(prompt.contains("Black or African American"));
        assertTrue(prompt.contains("Software Engineer"));
        assertTrue(prompt.contains("AI, robotics"));
    }

    @Test
    void buildPrompt_OmitsNullFields() {
        String prompt = matchPromptBuilder.buildPrompt(emptyProfile);

        assertFalse(prompt.contains("GPA:"));
        assertFalse(prompt.contains("Major:"));
        assertFalse(prompt.contains("State:"));
        assertFalse(prompt.contains("Ethnicity:"));
        assertFalse(prompt.contains("Career Goals:"));
        assertFalse(prompt.contains("Interests:"));
    }

    @Test
    void buildPrompt_ContainsFormattingRules() {
        String prompt = matchPromptBuilder.buildPrompt(fullProfile);

        assertTrue(prompt.contains("Do NOT return JSON"));
        assertTrue(prompt.contains("Do NOT use markdown"));
    }

    @Test
    void buildPrompt_ContainsOutputFormatInstructions() {
        String prompt = matchPromptBuilder.buildPrompt(fullProfile);
        assertTrue(prompt.contains("Return the results in this format"));
    }

    @Test
    void buildPrompt_EmptyProfileOnlyContainsBaseInstructions() {
        String prompt = matchPromptBuilder.buildPrompt(emptyProfile);
        assertTrue(prompt.contains("You are an AI scholarship recommendation assistant"));
        assertTrue(prompt.contains("STUDENT PROFILE:"));
    }

    @Test
    void buildPrompt_ContainsStudentProfileHeader() {
        String prompt = matchPromptBuilder.buildPrompt(fullProfile);
        assertTrue(prompt.contains("STUDENT PROFILE:"));
    }

    @Test
    void buildPrompt_NullGpaOmitsGpaLine() {
        fullProfile.setGpa(null);
        String prompt = matchPromptBuilder.buildPrompt(fullProfile);
        assertFalse(prompt.contains("GPA:"));
    }

    @Test
    void buildPrompt_NullMajorOmitsMajorLine() {
        fullProfile.setMajor(null);
        String prompt = matchPromptBuilder.buildPrompt(fullProfile);
        assertFalse(prompt.contains("Major:"));
    }

    @Test
    void buildPrompt_NullStatOmitsStateLine() {
        fullProfile.setState(null);
        String prompt = matchPromptBuilder.buildPrompt(fullProfile);
        assertFalse(prompt.contains("State:"));
    }

    @Test
    void buildPrompt_NullEthnicityOmitsEthnicityLine() {
        fullProfile.setEthnicity(null);
        String prompt = matchPromptBuilder.buildPrompt(fullProfile);
        assertFalse(prompt.contains("Ethnicity:"));
    }

    @Test
    void buildPrompt_NullCareerGoalsOmitsCareerGoalsLine() {
        fullProfile.setCareerGoals(null);
        String prompt = matchPromptBuilder.buildPrompt(fullProfile);
        assertFalse(prompt.contains("Career Goals:"));
    }

    @Test
    void buildPrompt_NullInterestsOmitsInterestsLine() {
        fullProfile.setInterests(null);
        String prompt = matchPromptBuilder.buildPrompt(fullProfile);
        assertFalse(prompt.contains("Interests:"));
    }

    @Test
    void buildPrompt_NullNameOmitsNameLine() {
        fullProfile.setName(null);
        String prompt = matchPromptBuilder.buildPrompt(fullProfile);
        assertFalse(prompt.contains("Name:"));
    }

    @Test
    void buildPrompt_NullEnrollmentStatusOmitsEnrollmentStatusLine() {
        fullProfile.setEnrollmentStatus(null);
        String prompt = matchPromptBuilder.buildPrompt(fullProfile);
        assertFalse(prompt.contains("Enrollment Status:"));
    }

    @Test
    void buildPrompt_NullNeedsFinancialAidOmitsFinancialAidLine() {
        fullProfile.setNeedsFinancialAid(null);
        String prompt = matchPromptBuilder.buildPrompt(fullProfile);
        assertFalse(prompt.contains("Needs Financial Aid:"));
    }
}