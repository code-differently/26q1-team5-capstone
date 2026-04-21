package com.scholarship.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scholarship.backend.entities.Profile;
import com.scholarship.backend.entities.User;
import com.scholarship.backend.services.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileController profileController;

    private ObjectMapper objectMapper;
    private User testUser;
    private Profile testProfile;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(profileController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        testUser = new User("jayden", "password123", "STUDENT");
        testUser.setUserId(1L);

        testProfile = new Profile();
        testProfile.setUser(testUser);
        testProfile.setName("Jayden");
        testProfile.setGpa(3.8);
        testProfile.setMajor("Computer Science");
        testProfile.setEnrollmentStatus("Full-time");
        testProfile.setNeedsFinancialAid(true);
        testProfile.setState("Delaware");
        testProfile.setEthnicity("Black or African American");
        testProfile.setCareerGoals("Software Engineer");
        testProfile.setInterests("AI, robotics");
    }

    @Test
    void createProfile_ReturnsOk() throws Exception {
        when(profileService.createProfile(any(Profile.class))).thenReturn(testProfile);

        mockMvc.perform(post("/api/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jayden"))
                .andExpect(jsonPath("$.major").value("Computer Science"));

        verify(profileService, times(1)).createProfile(any(Profile.class));
    }

    @Test
    void createProfile_ThrowsWhenProfileAlreadyExists() throws Exception {
        when(profileService.createProfile(any(Profile.class)))
                .thenThrow(new IllegalArgumentException("Profile already exists for user: 1"));

        mockMvc.perform(post("/api/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProfile)))
                .andExpect(status().isBadRequest());

        verify(profileService, times(1)).createProfile(any(Profile.class));
    }

    @Test
    void getProfile_ReturnsProfile() throws Exception {
        when(profileService.getProfile(1L)).thenReturn(testProfile);

        mockMvc.perform(get("/api/profiles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jayden"))
                .andExpect(jsonPath("$.gpa").value(3.8));

        verify(profileService, times(1)).getProfile(1L);
    }

    @Test
    void getProfile_ReturnsOkWithNullWhenNotFound() throws Exception {
        when(profileService.getProfile(99L)).thenReturn(null);

        mockMvc.perform(get("/api/profiles/99"))
                .andExpect(status().isOk());

        verify(profileService, times(1)).getProfile(99L);
    }

    @Test
    void updateProfile_ReturnsUpdatedProfile() throws Exception {
        Profile updatedProfile = new Profile();
        updatedProfile.setName("Jayden Updated");
        updatedProfile.setGpa(3.9);
        updatedProfile.setMajor("Software Engineering");
        updatedProfile.setEnrollmentStatus("Part-time");
        updatedProfile.setNeedsFinancialAid(false);
        updatedProfile.setState("California");
        updatedProfile.setEthnicity("Asian");
        updatedProfile.setCareerGoals("Tech Lead");
        updatedProfile.setInterests("Cloud computing");

        when(profileService.updateProfile(eq(1L), any(Profile.class))).thenReturn(updatedProfile);

        mockMvc.perform(put("/api/profiles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jayden Updated"))
                .andExpect(jsonPath("$.gpa").value(3.9))
                .andExpect(jsonPath("$.major").value("Software Engineering"));

        verify(profileService, times(1)).updateProfile(eq(1L), any(Profile.class));
    }

    @Test
    void updateProfile_ThrowsWhenProfileNotFound() throws Exception {
        when(profileService.updateProfile(eq(99L), any(Profile.class)))
                .thenThrow(new IllegalArgumentException("Profile not found with ID: 99"));

        mockMvc.perform(put("/api/profiles/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProfile)))
                .andExpect(status().isBadRequest());

        verify(profileService, times(1)).updateProfile(eq(99L), any(Profile.class));
    }

    @Test
    void deleteProfile_ReturnsNoContent() throws Exception {
        doNothing().when(profileService).deleteProfile(1L);

        mockMvc.perform(delete("/api/profiles/1"))
                .andExpect(status().isNoContent());

        verify(profileService, times(1)).deleteProfile(1L);
    }

    @Test
    void deleteProfile_ReturnsNotFoundWhenProfileMissing() throws Exception {
        doThrow(new IllegalArgumentException("Profile not found for user: 99"))
                .when(profileService).deleteProfile(99L);

        mockMvc.perform(delete("/api/profiles/99"))
                .andExpect(status().isNotFound());

        verify(profileService, times(1)).deleteProfile(99L);
    }

    @Test
    void deleteProfile_ReturnsInternalServerErrorOnUnexpectedException() throws Exception {
        doThrow(new RuntimeException("Unexpected error"))
                .when(profileService).deleteProfile(1L);

        mockMvc.perform(delete("/api/profiles/1"))
                .andExpect(status().isInternalServerError());

        verify(profileService, times(1)).deleteProfile(1L);
    }
}