package com.scholarship.backend.services;

import com.scholarship.backend.entities.Profile;
import com.scholarship.backend.entities.Scholarship;
import com.scholarship.backend.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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

    // --- buildPrompt ---

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
    void buildPrompt_ReturnsNonEmptyString() {
        String prompt = matchPromptBuilder.buildPrompt(fullProfile);
        assertNotNull(prompt);
        assertFalse(prompt.isBlank());
    }

    // --- parseResponse ---

    @Test
    void parseResponse_ValidJson_ReturnsScholarshipList() {
        String validJson = """
            [
              {
                "name": "NSF STEM Scholarship",
                "description": "For STEM students",
                "amount": 5000.0,
                "deadline": "2026-12-31",
                "eligibilityCriteria": "GPA 3.0+",
                "applicationUrl": "https://nsf.gov",
                "fieldOfStudy": "Computer Science",
                "state": "National"
              }
            ]
            """;

        List<Scholarship> result = matchPromptBuilder.parseResponse(validJson);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("NSF STEM Scholarship", result.get(0).getName());
        assertEquals(5000.0, result.get(0).getAmount());
        assertEquals("Computer Science", result.get(0).getFieldOfStudy());
        assertEquals("CLAUDE_AI", result.get(0).getSourceApi());
    }

    @Test
    void parseResponse_ValidJson_ParsesDeadlineCorrectly() {
        String validJson = """
            [
              {
                "name": "Test Scholarship",
                "deadline": "2026-06-15"
              }
            ]
            """;

        List<Scholarship> result = matchPromptBuilder.parseResponse(validJson);

        assertNotNull(result.get(0).getDeadline());
        assertEquals(2026, result.get(0).getDeadline().getYear());
        assertEquals(6, result.get(0).getDeadline().getMonthValue());
        assertEquals(15, result.get(0).getDeadline().getDayOfMonth());
    }

    @Test
    void parseResponse_NullDeadline_SetsDeadlineToNull() {
        String validJson = """
            [
              {
                "name": "Test Scholarship",
                "deadline": null
              }
            ]
            """;

        List<Scholarship> result = matchPromptBuilder.parseResponse(validJson);

        assertNull(result.get(0).getDeadline());
    }

    @Test
    void parseResponse_InvalidDeadlineFormat_SetsDeadlineToNull() {
        String validJson = """
            [
              {
                "name": "Test Scholarship",
                "deadline": "not-a-date"
              }
            ]
            """;

        List<Scholarship> result = matchPromptBuilder.parseResponse(validJson);

        assertNull(result.get(0).getDeadline());
    }

    @Test
    void parseResponse_NullName_SetsDefaultName() {
        String validJson = """
            [
              {
                "name": null
              }
            ]
            """;

        List<Scholarship> result = matchPromptBuilder.parseResponse(validJson);

        assertEquals("Unnamed Scholarship", result.get(0).getName());
    }

    @Test
    void parseResponse_SetsSourceApiToClaude() {
        String validJson = """
            [
              {
                "name": "Test Scholarship"
              }
            ]
            """;

        List<Scholarship> result = matchPromptBuilder.parseResponse(validJson);

        assertEquals("CLAUDE_AI", result.get(0).getSourceApi());
    }

    @Test
    void parseResponse_StripsMarkdownCodeBlocks() {
        String jsonWithMarkdown = """
```json
            [
              {
                "name": "Wrapped Scholarship",
                "amount": 1000.0
              }
            ]
```
            """;

        List<Scholarship> result = matchPromptBuilder.parseResponse(jsonWithMarkdown);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Wrapped Scholarship", result.get(0).getName());
    }

    @Test
    void parseResponse_InvalidJson_ReturnsEmptyList() {
        String invalidJson = "this is not json at all";

        List<Scholarship> result = matchPromptBuilder.parseResponse(invalidJson);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void parseResponse_EmptyArray_ReturnsEmptyList() {
        List<Scholarship> result = matchPromptBuilder.parseResponse("[]");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void parseResponse_MultipleScholarships_ReturnsAll() {
        String validJson = """
            [
              { "name": "Scholarship A", "amount": 1000.0 },
              { "name": "Scholarship B", "amount": 2000.0 },
              { "name": "Scholarship C", "amount": 3000.0 }
            ]
            """;

        List<Scholarship> result = matchPromptBuilder.parseResponse(validJson);

        assertEquals(3, result.size());
        assertEquals("Scholarship A", result.get(0).getName());
        assertEquals("Scholarship B", result.get(1).getName());
        assertEquals("Scholarship C", result.get(2).getName());
    }
}