package com.scholarship.backend.controllers;

import com.scholarship.backend.entities.Scholarship;
import com.scholarship.backend.services.ScholarshipQueryService;
import com.scholarship.backend.services.ScholarshipSyncService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ScholarshipControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ScholarshipQueryService scholarshipQueryService;

    @Mock
    private ScholarshipSyncService scholarshipSyncService;

    @InjectMocks
    private ScholarshipController scholarshipController;

    private Scholarship testScholarship;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(scholarshipController).build();

        testScholarship = new Scholarship("NSF STEM Scholarship", 5000.0, LocalDate.of(2027, 1, 1));
        testScholarship.setScholarshipId(1L);
    }

    // --- GET /api/scholarships/{id} ---

    @Test
    void getScholarshipById_ReturnsScholarship() throws Exception {
        when(scholarshipQueryService.getScholarshipById(1L)).thenReturn(testScholarship);

        mockMvc.perform(get("/api/scholarships/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NSF STEM Scholarship"))
                .andExpect(jsonPath("$.amount").value(5000.0));

        verify(scholarshipQueryService, times(1)).getScholarshipById(1L);
    }

    @Test
    void getScholarshipById_ReturnsOkWithNullWhenNotFound() throws Exception {
        when(scholarshipQueryService.getScholarshipById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/scholarships/99"))
                .andExpect(status().isOk());

        verify(scholarshipQueryService, times(1)).getScholarshipById(99L);
    }

    // --- GET /api/scholarships/search?query=... ---

    @Test
    void searchScholarships_ReturnsMatchingResults() throws Exception {
        when(scholarshipQueryService.searchScholarships("STEM")).thenReturn(List.of(testScholarship));

        mockMvc.perform(get("/api/scholarships/search").param("query", "STEM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("NSF STEM Scholarship"));

        verify(scholarshipQueryService, times(1)).searchScholarships("STEM");
    }

    @Test
    void searchScholarships_ReturnsEmptyListWhenNoMatches() throws Exception {
        when(scholarshipQueryService.searchScholarships("xyz123")).thenReturn(List.of());

        mockMvc.perform(get("/api/scholarships/search").param("query", "xyz123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // --- GET /api/scholarships ---

    @Test
    void getAllScholarships_ReturnsAllScholarships() throws Exception {
        Scholarship second = new Scholarship("Pell Grant", 7000.0, LocalDate.of(2027, 6, 1));
        second.setScholarshipId(2L);

        when(scholarshipQueryService.getAllScholarships()).thenReturn(List.of(testScholarship, second));

        mockMvc.perform(get("/api/scholarships"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("NSF STEM Scholarship"))
                .andExpect(jsonPath("$[1].name").value("Pell Grant"));

        verify(scholarshipQueryService, times(1)).getAllScholarships();
    }

    @Test
    void getAllScholarships_ReturnsEmptyListWhenNoneExist() throws Exception {
        when(scholarshipQueryService.getAllScholarships()).thenReturn(List.of());

        mockMvc.perform(get("/api/scholarships"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // --- POST /api/scholarships/sync ---

    @Test
    void syncFromApi_ReturnsSyncedScholarships() throws Exception {
        when(scholarshipSyncService.syncFromExternalApi()).thenReturn(List.of(testScholarship));

        mockMvc.perform(post("/api/scholarships/sync"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("NSF STEM Scholarship"));

        verify(scholarshipSyncService, times(1)).syncFromExternalApi();
    }

    @Test
    void syncFromApi_ReturnsEmptyListWhenNothingSynced() throws Exception {
        when(scholarshipSyncService.syncFromExternalApi()).thenReturn(List.of());

        mockMvc.perform(post("/api/scholarships/sync"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}