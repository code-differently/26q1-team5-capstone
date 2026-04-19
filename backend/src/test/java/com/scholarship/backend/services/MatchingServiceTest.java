package com.scholarship.backend.services;

import com.scholarship.backend.entities.Profile;
import com.scholarship.backend.entities.User;
import com.scholarship.backend.repositories.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchingServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private MatchPromptBuilder promptBuilder;

    @Mock
    private AIClient aiClient;

    @InjectMocks
    private MatchingService matchingService;

    private User testUser;
    private Profile testProfile;

    @BeforeEach
    void setUp() {
        testUser = new User("jayden", "password123", "STUDENT");
        testProfile = new Profile();
        testProfile.setUser(testUser);
        testProfile.setName("Jayden");
        testProfile.setMajor("Computer Science");
        testProfile.setGpa(3.8);
        testProfile.setState("Delaware");
    }

    // --- getAIScholarshipSearch ---

    @Test
    void getAIScholarshipSearch_Success() {
        String mockPrompt = "Find scholarships for a Computer Science student";
        String mockResponse = "[{\"name\": \"NSF Scholarship\", \"amount\": 5000}]";

        when(profileRepository.findByUser_UserId(1L)).thenReturn(testProfile);
        when(promptBuilder.buildPrompt(testProfile)).thenReturn(mockPrompt);
        when(aiClient.getCompletion(mockPrompt)).thenReturn(mockResponse);

        String result = matchingService.getAIScholarshipSearch(1L);

        assertNotNull(result);
        assertEquals(mockResponse, result);
        verify(profileRepository, times(1)).findByUser_UserId(1L);
        verify(promptBuilder, times(1)).buildPrompt(testProfile);
        verify(aiClient, times(1)).getCompletion(mockPrompt);
    }

    @Test
    void getAIScholarshipSearch_ThrowsWhenProfileNotFound() {
        when(profileRepository.findByUser_UserId(99L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(
            IllegalArgumentException.class,
            () -> matchingService.getAIScholarshipSearch(99L)
        );

        assertTrue(ex.getMessage().contains("Profile not found for user"));
        verify(promptBuilder, never()).buildPrompt(any());
        verify(aiClient, never()).getCompletion(any());
    }

    @Test
    void getAIScholarshipSearch_ReturnsEmptyArrayWhenAIFails() {
        String mockPrompt = "Find scholarships for a Computer Science student";

        when(profileRepository.findByUser_UserId(1L)).thenReturn(testProfile);
        when(promptBuilder.buildPrompt(testProfile)).thenReturn(mockPrompt);
        when(aiClient.getCompletion(mockPrompt)).thenReturn("[]");

        String result = matchingService.getAIScholarshipSearch(1L);

        assertEquals("[]", result);
    }

    @Test
    void getAIScholarshipSearch_PassesCorrectPromptToAIClient() {
        String mockPrompt = "Find scholarships for a Computer Science student in Delaware";

        when(profileRepository.findByUser_UserId(1L)).thenReturn(testProfile);
        when(promptBuilder.buildPrompt(testProfile)).thenReturn(mockPrompt);
        when(aiClient.getCompletion(mockPrompt)).thenReturn("[]");

        matchingService.getAIScholarshipSearch(1L);

        verify(aiClient, times(1)).getCompletion(mockPrompt);
    }
}