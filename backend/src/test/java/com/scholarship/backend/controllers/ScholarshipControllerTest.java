package com.scholarship.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scholarship.backend.entities.Scholarship;
import com.scholarship.backend.services.ScholarshipQueryService;
import com.scholarship.backend.services.ScholarshipSyncService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScholarshipController.class)
public class ScholarshipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScholarshipQueryService scholarshipQueryService;

    @MockBean
    private ScholarshipSyncService scholarshipSyncService;

    @Autowired
    private ObjectMapper objectMapper;

    private Scholarship testScholarship;
    private List<Scholarship> scholarshipList;

    @BeforeEach
    public void setUp() {
        testScholarship = new Scholarship();
        testScholarship.setScholarshipId(1L);
        testScholarship.setName("Tech Leaders Scholarship");
        testScholarship.setDescription("Scholarship for computer science students");
        testScholarship.setAmount(5000.0);
        testScholarship.setDeadline(LocalDate.of(2026, 6, 30));
        testScholarship.setEligibilityCriteria("GPA >= 3.5, CS Major");
        testScholarship.setApplicationUrl("https://example.com/apply");
        testScholarship.setFieldOfStudy("Computer Science");
        testScholarship.setState("California");
        testScholarship.setSourceApi("scholarships_api");

        scholarshipList = new ArrayList<>();
        scholarshipList.add(testScholarship);
    }

    @Test
    public void testGetScholarshipByIdSuccess() throws Exception {
        when(scholarshipQueryService.getScholarshipById(anyLong())).thenReturn(testScholarship);

        mockMvc.perform(get("/api/scholarships/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scholarshipId").value(1))
                .andExpect(jsonPath("$.name").value("Tech Leaders Scholarship"))
                .andExpect(jsonPath("$.amount").value(5000.0))
                .andExpect(jsonPath("$.fieldOfStudy").value("Computer Science"));

        verify(scholarshipQueryService, times(1)).getScholarshipById(1L);
    }

    @Test
    public void testSearchScholarshipsSuccess() throws Exception {
        when(scholarshipQueryService.searchScholarships(anyString())).thenReturn(scholarshipList);

        mockMvc.perform(get("/api/scholarships/search?query=computer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Tech Leaders Scholarship"))
                .andExpect(jsonPath("$[0].fieldOfStudy").value("Computer Science"));

        verify(scholarshipQueryService, times(1)).searchScholarships("computer");
    }

    @Test
    public void testSearchScholarshipsEmptyResult() throws Exception {
        List<Scholarship> emptyList = new ArrayList<>();
        when(scholarshipQueryService.searchScholarships(anyString())).thenReturn(emptyList);

        mockMvc.perform(get("/api/scholarships/search?query=nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(0)));

        verify(scholarshipQueryService, times(1)).searchScholarships("nonexistent");
    }

    @Test
    public void testSyncFromApiSuccess() throws Exception {
        when(scholarshipSyncService.syncFromExternalApi()).thenReturn(scholarshipList);

        mockMvc.perform(post("/api/scholarships/sync")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].scholarshipId").value(1))
                .andExpect(jsonPath("$[0].name").value("Tech Leaders Scholarship"));

        verify(scholarshipSyncService, times(1)).syncFromExternalApi();
    }

    @Test
    public void testGetScholarshipNotFound() throws Exception {
        when(scholarshipQueryService.getScholarshipById(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/scholarships/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(scholarshipQueryService, times(1)).getScholarshipById(999L);
    }

    @Test
    public void testSearchWithMultipleResults() throws Exception {
        Scholarship scholarship2 = new Scholarship();
        scholarship2.setScholarshipId(2L);
        scholarship2.setName("AI Innovators Fund");
        scholarship2.setFieldOfStudy("Artificial Intelligence");

        List<Scholarship> multipleResults = new ArrayList<>();
        multipleResults.add(testScholarship);
        multipleResults.add(scholarship2);

        when(scholarshipQueryService.searchScholarships(anyString())).thenReturn(multipleResults);

        mockMvc.perform(get("/api/scholarships/search?query=tech")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Tech Leaders Scholarship"))
                .andExpect(jsonPath("$[1].name").value("AI Innovators Fund"));

        verify(scholarshipQueryService, times(1)).searchScholarships("tech");
    }

    @Test
    public void testSyncFromApiReturnsMultiple() throws Exception {
        Scholarship scholarship2 = new Scholarship();
        scholarship2.setScholarshipId(2L);
        scholarship2.setName("Medical Excellence Award");

        List<Scholarship> syncedScholarships = new ArrayList<>();
        syncedScholarships.add(testScholarship);
        syncedScholarships.add(scholarship2);

        when(scholarshipSyncService.syncFromExternalApi()).thenReturn(syncedScholarships);

        mockMvc.perform(post("/api/scholarships/sync")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)));

        verify(scholarshipSyncService, times(1)).syncFromExternalApi();
    }
}

