package com.scholarship.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scholarship.backend.entities.Application;
import com.scholarship.backend.entities.ApplicationStatus;
import com.scholarship.backend.entities.Scholarship;
import com.scholarship.backend.entities.User;
import com.scholarship.backend.services.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApplicationController.class)
public class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Application testApplication;

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");

        Scholarship scholarship = new Scholarship();
        scholarship.setScholarshipId(1L);
        scholarship.setName("Tech Leaders Scholarship");
        scholarship.setAmount(5000.0);

        testApplication = new Application();
        testApplication.setApplicationId(1L);
        testApplication.setUser(user);
        testApplication.setScholarship(scholarship);
        testApplication.setStatus(ApplicationStatus.SAVED);
        testApplication.setSavedDate(LocalDate.now());
        testApplication.setNotes("Great opportunity");
    }

    @Test
    public void testCreateApplicationSuccess() throws Exception {
        Map<String, Long> request = new HashMap<>();
        request.put("userId", 1L);
        request.put("scholarshipId", 1L);

        when(applicationService.createApplication(anyLong(), anyLong())).thenReturn(testApplication);

        mockMvc.perform(post("/api/applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationId").value(1))
                .andExpect(jsonPath("$.status").value("SAVED"));

        verify(applicationService, times(1)).createApplication(1L, 1L);
    }

    @Test
    public void testUpdateStatusSuccess() throws Exception {
        Application updatedApplication = new Application();
        updatedApplication.setApplicationId(1L);
        updatedApplication.setStatus(ApplicationStatus.SUBMITTED);
        updatedApplication.setSubmittedDate(LocalDate.now());

        Map<String, String> request = new HashMap<>();
        request.put("status", "SUBMITTED");

        when(applicationService.updateStatus(anyLong(), any(ApplicationStatus.class)))
                .thenReturn(updatedApplication);

        mockMvc.perform(put("/api/applications/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationId").value(1))
                .andExpect(jsonPath("$.status").value("SUBMITTED"));

        verify(applicationService, times(1)).updateStatus(anyLong(), any(ApplicationStatus.class));
    }

    @Test
    public void testGetApplicationsSuccess() throws Exception {
        List<Application> applications = new ArrayList<>();
        applications.add(testApplication);

        when(applicationService.getApplications(anyLong())).thenReturn(applications);

        mockMvc.perform(get("/api/applications/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].applicationId").value(1))
                .andExpect(jsonPath("$[0].status").value("SAVED"));

        verify(applicationService, times(1)).getApplications(1L);
    }

    @Test
    public void testDeleteApplicationSuccess() throws Exception {
        doNothing().when(applicationService).deleteApplication(anyLong());

        mockMvc.perform(delete("/api/applications/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(applicationService, times(1)).deleteApplication(1L);
    }

    @Test
    public void testGetApplicationsEmptyList() throws Exception {
        List<Application> emptyList = new ArrayList<>();
        when(applicationService.getApplications(anyLong())).thenReturn(emptyList);

        mockMvc.perform(get("/api/applications/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(0)));

        verify(applicationService, times(1)).getApplications(999L);
    }

    @Test
    public void testUpdateStatusToAwarded() throws Exception {
        Application awardedApplication = new Application();
        awardedApplication.setApplicationId(1L);
        awardedApplication.setStatus(ApplicationStatus.AWARDED);

        Map<String, String> request = new HashMap<>();
        request.put("status", "AWARDED");

        when(applicationService.updateStatus(anyLong(), any(ApplicationStatus.class)))
                .thenReturn(awardedApplication);

        mockMvc.perform(put("/api/applications/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("AWARDED"));

        verify(applicationService, times(1)).updateStatus(anyLong(), any(ApplicationStatus.class));
    }

    @Test
    public void testGetMultipleApplications() throws Exception {
        Application app2 = new Application();
        app2.setApplicationId(2L);
        app2.setStatus(ApplicationStatus.IN_PROGRESS);

        List<Application> applications = new ArrayList<>();
        applications.add(testApplication);
        applications.add(app2);

        when(applicationService.getApplications(anyLong())).thenReturn(applications);

        mockMvc.perform(get("/api/applications/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].status").value("SAVED"))
                .andExpect(jsonPath("$[1].status").value("IN_PROGRESS"));

        verify(applicationService, times(1)).getApplications(1L);
    }

    @Test
    public void testCreateApplicationWithDifferentScholarships() throws Exception {
        Map<String, Long> request = new HashMap<>();
        request.put("userId", 2L);
        request.put("scholarshipId", 5L);

        Application newApplication = new Application();
        newApplication.setApplicationId(2L);
        newApplication.setStatus(ApplicationStatus.SAVED);

        when(applicationService.createApplication(anyLong(), anyLong())).thenReturn(newApplication);

        mockMvc.perform(post("/api/applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationId").value(2));

        verify(applicationService, times(1)).createApplication(2L, 5L);
    }
}

