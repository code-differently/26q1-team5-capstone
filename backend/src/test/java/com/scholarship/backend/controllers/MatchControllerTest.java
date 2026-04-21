package com.scholarship.backend.controllers;

import com.scholarship.backend.services.MatchingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MatchControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MatchingService matchingService;

    @InjectMocks
    private MatchController matchController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(matchController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAIScholarshipSearch_ReturnsOk() throws Exception {
        when(matchingService.getAIScholarshipSearch(1L)).thenReturn("Here are some scholarships for you.");

        mockMvc.perform(get("/api/matches/1/ai-search"))
                .andExpect(status().isOk())
                .andExpect(content().string("Here are some scholarships for you."));

        verify(matchingService, times(1)).getAIScholarshipSearch(1L);
    }

    @Test
    void getAIScholarshipSearch_ReturnsEmptyString() throws Exception {
        when(matchingService.getAIScholarshipSearch(1L)).thenReturn("");

        mockMvc.perform(get("/api/matches/1/ai-search"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(matchingService, times(1)).getAIScholarshipSearch(1L);
    }

    @Test
    void getAIScholarshipSearch_ThrowsWhenProfileNotFound() throws Exception {
        when(matchingService.getAIScholarshipSearch(99L))
                .thenThrow(new IllegalArgumentException("Profile not found for user: 99"));

        mockMvc.perform(get("/api/matches/99/ai-search"))
                .andExpect(status().isBadRequest());

        verify(matchingService, times(1)).getAIScholarshipSearch(99L);
    }
}