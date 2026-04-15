package com.scholarship.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scholarship.backend.entities.Profile;
import com.scholarship.backend.services.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

@WebMvcTest(ProfileController.class)
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @Autowired
    private ObjectMapper objectMapper;

    private Profile testProfile;

    @BeforeEach
    public void setUp() {
        testProfile = new Profile();
        testProfile.setProfileId(1L);
        testProfile.setUserId(1L);
        testProfile.setName("John Doe");
        testProfile.setGpa(3.8);
        testProfile.setMajor("Computer Science");
        testProfile.setEnrollmentStatus("Full-Time");
        testProfile.setNeedsFinancialAid(true);
        testProfile.setState("California");
        testProfile.setEthnicity("Asian");
        testProfile.setCareerGoals("Software Engineer");
        testProfile.setInterests("AI, Web Development");
    }

    @Test
    public void testCreateProfileSuccess() throws Exception {
        when(profileService.createProfile(any(Profile.class))).thenReturn(testProfile);

        mockMvc.perform(post("/api/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileId").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.major").value("Computer Science"));

        verify(profileService, times(1)).createProfile(any(Profile.class));
    }

    @Test
    public void testGetProfileSuccess() throws Exception {
        when(profileService.getProfile(anyLong())).thenReturn(testProfile);

        mockMvc.perform(get("/api/profiles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileId").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.gpa").value(3.8));

        verify(profileService, times(1)).getProfile(1L);
    }

    @Test
    public void testUpdateProfileSuccess() throws Exception {
        Profile updatedProfile = new Profile();
        updatedProfile.setProfileId(1L);
        updatedProfile.setName("Jane Doe");
        updatedProfile.setGpa(4.0);
        updatedProfile.setMajor("Data Science");

        when(profileService.updateProfile(anyLong(), any(Profile.class))).thenReturn(updatedProfile);

        mockMvc.perform(put("/api/profiles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.gpa").value(4.0))
                .andExpect(jsonPath("$.major").value("Data Science"));

        verify(profileService, times(1)).updateProfile(anyLong(), any(Profile.class));
    }

    @Test
    public void testDeleteProfileSuccess() throws Exception {
        doNothing().when(profileService).deleteProfile(anyLong());

        mockMvc.perform(delete("/api/profiles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(profileService, times(1)).deleteProfile(1L);
    }

    @Test
    public void testCreateProfileWithMinimalData() throws Exception {
        Profile minimalProfile = new Profile();
        minimalProfile.setProfileId(2L);
        minimalProfile.setUserId(2L);
        minimalProfile.setName("Minimal User");

        when(profileService.createProfile(any(Profile.class))).thenReturn(minimalProfile);

        mockMvc.perform(post("/api/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(minimalProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Minimal User"));

        verify(profileService, times(1)).createProfile(any(Profile.class));
    }

    @Test
    public void testGetProfileNotFound() throws Exception {
        when(profileService.getProfile(anyLong())).thenReturn(null);

        mockMvc.perform(get("/api/profiles/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(profileService, times(1)).getProfile(999L);
    }
}

