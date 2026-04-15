package com.scholarship.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scholarship.backend.entities.Scholarship;
import com.scholarship.backend.services.MatchingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatchController.class)
public class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchingService matchingService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Scholarship> testMatches;

    @BeforeEach
    public void setUp() {
        testMatches = new ArrayList<>();

        Scholarship scholarship1 = new Scholarship();
        scholarship1.setScholarshipId(1L);
        scholarship1.setName("Tech Leaders Scholarship");
        scholarship1.setAmount(5000.0);
        scholarship1.setFieldOfStudy("Computer Science");
        scholarship1.setDeadline(LocalDate.of(2026, 6, 30));

        Scholarship scholarship2 = new Scholarship();
        scholarship2.setScholarshipId(2L);
        scholarship2.setName("AI Innovators Fund");
        scholarship2.setAmount(7500.0);
        scholarship2.setFieldOfStudy("Artificial Intelligence");
        scholarship2.setDeadline(LocalDate.of(2026, 5, 15));

        testMatches.add(scholarship1);
        testMatches.add(scholarship2);
    }

    @Test
    public void testGetMatchesSuccess() throws Exception {
        when(matchingService.getMatchesForUser(anyLong())).thenReturn(testMatches);

        mockMvc.perform(get("/api/matches/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].scholarshipId").value(1))
                .andExpect(jsonPath("$[0].name").value("Tech Leaders Scholarship"))
                .andExpect(jsonPath("$[1].scholarshipId").value(2))
                .andExpect(jsonPath("$[1].name").value("AI Innovators Fund"));

        verify(matchingService, times(1)).getMatchesForUser(1L);
    }

    @Test
    public void testGetMatchesEmptyList() throws Exception {
        List<Scholarship> emptyMatches = new ArrayList<>();
        when(matchingService.getMatchesForUser(anyLong())).thenReturn(emptyMatches);

        mockMvc.perform(get("/api/matches/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(0)));

        verify(matchingService, times(1)).getMatchesForUser(999L);
    }

    @Test
    public void testRefreshMatchesSuccess() throws Exception {
        doNothing().when(matchingService).refreshMatchesForUser(anyLong());

        mockMvc.perform(post("/api/matches/1/refresh")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(matchingService, times(1)).refreshMatchesForUser(1L);
    }

    @Test
    public void testGetMatchesSingleResult() throws Exception {
        List<Scholarship> singleMatch = new ArrayList<>();
        Scholarship scholarship = new Scholarship();
        scholarship.setScholarshipId(1L);
        scholarship.setName("Tech Leaders Scholarship");
        scholarship.setAmount(5000.0);
        singleMatch.add(scholarship);

        when(matchingService.getMatchesForUser(anyLong())).thenReturn(singleMatch);

        mockMvc.perform(get("/api/matches/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Tech Leaders Scholarship"));

        verify(matchingService, times(1)).getMatchesForUser(2L);
    }

    @Test
    public void testRefreshMatchesForMultipleUsers() throws Exception {
        doNothing().when(matchingService).refreshMatchesForUser(anyLong());

        // Refresh for user 1
        mockMvc.perform(post("/api/matches/1/refresh")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Refresh for user 2
        mockMvc.perform(post("/api/matches/2/refresh")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(matchingService, times(1)).refreshMatchesForUser(1L);
        verify(matchingService, times(1)).refreshMatchesForUser(2L);
    }

    @Test
    public void testGetMatchesWithAllFields() throws Exception {
        List<Scholarship> matches = new ArrayList<>();
        Scholarship scholarship = new Scholarship();
        scholarship.setScholarshipId(1L);
        scholarship.setName("Comprehensive Scholarship");
        scholarship.setDescription("A comprehensive scholarship program");
        scholarship.setAmount(10000.0);
        scholarship.setDeadline(LocalDate.of(2026, 12, 31));
        scholarship.setEligibilityCriteria("GPA >= 3.0, US Citizen");
        scholarship.setApplicationUrl("https://example.com/apply");
        scholarship.setFieldOfStudy("Engineering");
        scholarship.setState("New York");
        scholarship.setSourceApi("external_api");

        matches.add(scholarship);

        when(matchingService.getMatchesForUser(anyLong())).thenReturn(matches);

        mockMvc.perform(get("/api/matches/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].scholarshipId").value(1))
                .andExpect(jsonPath("$[0].name").value("Comprehensive Scholarship"))
                .andExpect(jsonPath("$[0].amount").value(10000.0))
                .andExpect(jsonPath("$[0].fieldOfStudy").value("Engineering"));

        verify(matchingService, times(1)).getMatchesForUser(5L);
    }

    @Test
    public void testGetMatchesMultipleCallsDifferentUsers() throws Exception {
        when(matchingService.getMatchesForUser(1L)).thenReturn(testMatches);

        List<Scholarship> differentMatches = new ArrayList<>();
        Scholarship medicalScholarship = new Scholarship();
        medicalScholarship.setScholarshipId(10L);
        medicalScholarship.setName("Medical Excellence Award");
        differentMatches.add(medicalScholarship);

        when(matchingService.getMatchesForUser(2L)).thenReturn(differentMatches);

        mockMvc.perform(get("/api/matches/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)));

        mockMvc.perform(get("/api/matches/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Medical Excellence Award"));

        verify(matchingService, times(1)).getMatchesForUser(1L);
        verify(matchingService, times(1)).getMatchesForUser(2L);
    }
}

