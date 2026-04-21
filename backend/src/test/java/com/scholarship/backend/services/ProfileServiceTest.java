package com.scholarship.backend.services;

import com.scholarship.backend.entities.Profile;
import com.scholarship.backend.entities.User;
import com.scholarship.backend.repositories.ProfileRepository;
import com.scholarship.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProfileService profileService;

    private User testUser;
    private Profile testProfile;

    @BeforeEach
    void setUp() {
        testUser = new User("jayden", "password123", "STUDENT");
        testUser.setUserId(1L); // add this line
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

    // --- createProfile ---

    @Test
    void createProfile_Success() {
        when(profileRepository.findByUser_UserId(testUser.getUserId())).thenReturn(null);
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        Profile result = profileService.createProfile(testProfile);

        assertNotNull(result);
        assertEquals("Jayden", result.getName());
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void createProfile_ThrowsWhenProfileAlreadyExists() {
        when(profileRepository.findByUser_UserId(testUser.getUserId())).thenReturn(testProfile);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> profileService.createProfile(testProfile));

        assertTrue(ex.getMessage().contains("Profile already exists"));
        verify(profileRepository, never()).save(any(Profile.class));
    }

    // --- getProfile ---

    @Test
    void getProfile_ReturnsProfileWhenFound() {
        when(profileRepository.findByUser_UserId(1L)).thenReturn(testProfile);

        Profile result = profileService.getProfile(1L);

        assertNotNull(result);
        assertEquals("Jayden", result.getName());
    }

    @Test
    void getProfile_ReturnsNullWhenNotFound() {
        when(profileRepository.findByUser_UserId(99L)).thenReturn(null);

        Profile result = profileService.getProfile(99L);

        assertNull(result);
    }

    // --- updateProfile ---

    @Test
    void updateProfile_Success() {
        Profile updatedData = new Profile();
        updatedData.setName("Jayden Updated");
        updatedData.setGpa(3.9);
        updatedData.setMajor("Software Engineering");
        updatedData.setEnrollmentStatus("Part-time");
        updatedData.setNeedsFinancialAid(false);
        updatedData.setState("California");
        updatedData.setEthnicity("Asian");
        updatedData.setCareerGoals("Tech Lead");
        updatedData.setInterests("Cloud computing");

        when(profileRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        Profile result = profileService.updateProfile(1L, updatedData);

        assertNotNull(result);
        assertEquals("Jayden Updated", result.getName());
        assertEquals(3.9, result.getGpa());
        assertEquals("Software Engineering", result.getMajor());
        assertEquals("Part-time", result.getEnrollmentStatus());
        assertFalse(result.getNeedsFinancialAid());
        assertEquals("California", result.getState());
        assertEquals("Asian", result.getEthnicity());
        assertEquals("Tech Lead", result.getCareerGoals());
        assertEquals("Cloud computing", result.getInterests());
        verify(profileRepository, times(1)).save(any(Profile.class));
    }

    @Test
    void updateProfile_ThrowsWhenProfileNotFound() {
        when(profileRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> profileService.updateProfile(99L, testProfile));

        assertTrue(ex.getMessage().contains("Profile not found with ID"));
        verify(profileRepository, never()).save(any(Profile.class));
    }

    // --- deleteProfile ---

    @Test
    void deleteProfile_Success() {
        when(profileRepository.findByUser_UserId(1L)).thenReturn(testProfile);
        doNothing().when(userRepository).deleteById(1L);

        profileService.deleteProfile(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProfile_ThrowsWhenProfileNotFound() {
        when(profileRepository.findByUser_UserId(99L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> profileService.deleteProfile(99L));

        assertTrue(ex.getMessage().contains("Profile not found for user"));
        verify(userRepository, never()).deleteById(any());
    }
}