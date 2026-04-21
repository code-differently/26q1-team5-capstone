package com.scholarship.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scholarship.backend.entities.*;
import com.scholarship.backend.services.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ApplicationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ApplicationService applicationService;

    @InjectMocks
    private ApplicationController applicationController;

    private ObjectMapper objectMapper;
    private User testUser;
    private Scholarship testScholarship;
    private Application testApplication;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(applicationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        testUser = new User("jayden", "password123", "STUDENT");
        testUser.setUserId(1L);

        testScholarship = new Scholarship("NSF STEM Scholarship", 5000.0, LocalDate.of(2027, 1, 1));
        testScholarship.setScholarshipId(1L);

        testApplication = new Application(testUser, testScholarship);
    }

    @Test
    void createApplication_ReturnsOk() throws Exception {
        when(applicationService.createApplication(1L, 1L)).thenReturn(testApplication);

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("userId", 1L, "scholarshipId", 1L))))
                .andExpect(status().isOk());

        verify(applicationService, times(1)).createApplication(1L, 1L);
    }

    @Test
    void createApplication_ThrowsWhenUserNotFound() throws Exception {
        when(applicationService.createApplication(99L, 1L))
                .thenThrow(new IllegalArgumentException("User not found with ID: 99"));

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("userId", 99L, "scholarshipId", 1L))))
                .andExpect(status().isBadRequest());

        verify(applicationService, times(1)).createApplication(99L, 1L);
    }

    @Test
    void createApplication_ThrowsWhenScholarshipNotFound() throws Exception {
        when(applicationService.createApplication(1L, 99L))
                .thenThrow(new IllegalArgumentException("Scholarship not found with ID: 99"));

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("userId", 1L, "scholarshipId", 99L))))
                .andExpect(status().isBadRequest());

        verify(applicationService, times(1)).createApplication(1L, 99L);
    }

    @Test
    void createApplication_ThrowsWhenDuplicateApplication() throws Exception {
        when(applicationService.createApplication(1L, 1L))
                .thenThrow(new IllegalArgumentException("You already have an application for this scholarship"));

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("userId", 1L, "scholarshipId", 1L))))
                .andExpect(status().isBadRequest());

        verify(applicationService, times(1)).createApplication(1L, 1L);
    }

    @Test
    void updateStatus_ReturnsUpdatedApplication() throws Exception {
        testApplication.setStatus(ApplicationStatus.IN_PROGRESS);
        when(applicationService.updateStatus(1L, ApplicationStatus.SUBMITTED)).thenReturn(testApplication);

        mockMvc.perform(put("/api/applications/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", "SUBMITTED"))))
                .andExpect(status().isOk());

        verify(applicationService, times(1)).updateStatus(1L, ApplicationStatus.SUBMITTED);
    }

    @Test
    void updateStatus_ThrowsWhenApplicationNotFound() throws Exception {
        when(applicationService.updateStatus(99L, ApplicationStatus.SUBMITTED))
                .thenThrow(new IllegalArgumentException("Application not found with ID: 99"));

        mockMvc.perform(put("/api/applications/99/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", "SUBMITTED"))))
                .andExpect(status().isBadRequest());

        verify(applicationService, times(1)).updateStatus(99L, ApplicationStatus.SUBMITTED);
    }

    @Test
    void updateStatus_ThrowsOnInvalidStatus() throws Exception {
        mockMvc.perform(put("/api/applications/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", "INVALID_STATUS"))))
                .andExpect(status().isBadRequest());

        verify(applicationService, never()).updateStatus(anyLong(), any());
    }

    @Test
    void getApplications_ReturnsListForUser() throws Exception {
        when(applicationService.getApplications(1L)).thenReturn(List.of(testApplication));

        mockMvc.perform(get("/api/applications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(applicationService, times(1)).getApplications(1L);
    }

    @Test
    void getApplications_ReturnsEmptyListWhenNoneFound() throws Exception {
        when(applicationService.getApplications(99L)).thenReturn(List.of());

        mockMvc.perform(get("/api/applications/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(applicationService, times(1)).getApplications(99L);
    }

    @Test
    void deleteApplication_ReturnsNoContent() throws Exception {
        doNothing().when(applicationService).deleteApplication(1L);

        mockMvc.perform(delete("/api/applications/1"))
                .andExpect(status().isNoContent());

        verify(applicationService, times(1)).deleteApplication(1L);
    }

    @Test
    void deleteApplication_ThrowsWhenApplicationNotFound() throws Exception {
        doThrow(new IllegalArgumentException("Application not found with ID: 99"))
                .when(applicationService).deleteApplication(99L);

        mockMvc.perform(delete("/api/applications/99"))
                .andExpect(status().isBadRequest());

        verify(applicationService, times(1)).deleteApplication(99L);
    }
}