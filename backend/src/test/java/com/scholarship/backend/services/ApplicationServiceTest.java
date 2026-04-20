package com.scholarship.backend.services;

import com.scholarship.backend.entities.*;
import com.scholarship.backend.repositories.ApplicationRepository;
import com.scholarship.backend.repositories.ScholarshipRepository;
import com.scholarship.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ScholarshipRepository scholarshipRepository;

    @InjectMocks
    private ApplicationService applicationService;

    private User testUser;
    private Scholarship testScholarship;
    private Application testApplication;

    @BeforeEach
    void setUp() {
        testUser = new User("jayden", "password123", "STUDENT");
        testUser.setUserId(1L); // add this line
        testScholarship = new Scholarship("NSF STEM Scholarship", 5000.0, LocalDate.of(2027, 1, 1));
        testScholarship.setScholarshipId(1L); // add this line
        testApplication = new Application(testUser, testScholarship);
    }

    // --- createApplication ---

    @Test
    void createApplication_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(scholarshipRepository.findById(1L)).thenReturn(Optional.of(testScholarship));
        when(applicationRepository.findByUser_UserId(1L)).thenReturn(List.of());
        when(applicationRepository.save(any(Application.class))).thenReturn(testApplication);

        Application result = applicationService.createApplication(1L, 1L);

        assertNotNull(result);
        verify(applicationRepository, times(1)).save(any(Application.class));
    }

    @Test
    void createApplication_ThrowsWhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> applicationService.createApplication(99L, 1L));

        assertTrue(ex.getMessage().contains("User not found"));
        verify(applicationRepository, never()).save(any(Application.class));
    }

    @Test
    void createApplication_ThrowsWhenScholarshipNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(scholarshipRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> applicationService.createApplication(1L, 99L));

        assertTrue(ex.getMessage().contains("Scholarship not found"));
        verify(applicationRepository, never()).save(any(Application.class));
    }

    @Test
    void createApplication_ThrowsWhenDuplicateApplication() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(scholarshipRepository.findById(1L)).thenReturn(Optional.of(testScholarship));
        when(applicationRepository.findByUser_UserId(1L)).thenReturn(List.of(testApplication));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> applicationService.createApplication(1L, testScholarship.getScholarshipId()));

        assertTrue(ex.getMessage().contains("already have an application"));
        verify(applicationRepository, never()).save(any(Application.class));
    }

    // --- updateStatus ---

    @Test
    void updateStatus_Success() {
        testApplication.setStatus(ApplicationStatus.SAVED);
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(testApplication));
        when(applicationRepository.save(any(Application.class))).thenReturn(testApplication);

        Application result = applicationService.updateStatus(1L, ApplicationStatus.IN_PROGRESS);

        assertNotNull(result);
        assertEquals(ApplicationStatus.IN_PROGRESS, result.getStatus());
        verify(applicationRepository, times(1)).save(any(Application.class));
    }

    @Test
    void updateStatus_SetsSubmittedDateWhenMovingToSubmitted() {
        testApplication.setStatus(ApplicationStatus.IN_PROGRESS);
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(testApplication));
        when(applicationRepository.save(any(Application.class))).thenReturn(testApplication);

        Application result = applicationService.updateStatus(1L, ApplicationStatus.SUBMITTED);

        assertNotNull(result.getSubmittedDate());
        assertEquals(LocalDate.now(), result.getSubmittedDate());
    }

    @Test
    void updateStatus_ThrowsWhenApplicationNotFound() {
        when(applicationRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> applicationService.updateStatus(99L, ApplicationStatus.SUBMITTED));

        assertTrue(ex.getMessage().contains("Application not found"));
        verify(applicationRepository, never()).save(any(Application.class));
    }

    // --- validateTransition ---

    @Test
    void validateTransition_SameStatusIsNoOp() {
        assertDoesNotThrow(
                () -> applicationService.validateTransition(ApplicationStatus.SAVED, ApplicationStatus.SAVED));
    }

    @Test
    void validateTransition_SavedToInProgress() {
        assertDoesNotThrow(
                () -> applicationService.validateTransition(ApplicationStatus.SAVED, ApplicationStatus.IN_PROGRESS));
    }

    @Test
    void validateTransition_SavedToSubmitted() {
        assertDoesNotThrow(
                () -> applicationService.validateTransition(ApplicationStatus.SAVED, ApplicationStatus.SUBMITTED));
    }

    @Test
    void validateTransition_SavedToAwarded() {
        assertDoesNotThrow(
                () -> applicationService.validateTransition(ApplicationStatus.SAVED, ApplicationStatus.AWARDED));
    }

    @Test
    void validateTransition_SavedToRejected() {
        assertDoesNotThrow(
                () -> applicationService.validateTransition(ApplicationStatus.SAVED, ApplicationStatus.REJECTED));
    }

    @Test
    void validateTransition_InProgressToSubmitted() {
        assertDoesNotThrow(() -> applicationService.validateTransition(ApplicationStatus.IN_PROGRESS,
                ApplicationStatus.SUBMITTED));
    }

    @Test
    void validateTransition_InProgressToSaved() {
        assertDoesNotThrow(
                () -> applicationService.validateTransition(ApplicationStatus.IN_PROGRESS, ApplicationStatus.SAVED));
    }

    @Test
    void validateTransition_SubmittedToAwarded() {
        assertDoesNotThrow(
                () -> applicationService.validateTransition(ApplicationStatus.SUBMITTED, ApplicationStatus.AWARDED));
    }

    @Test
    void validateTransition_SubmittedToRejected() {
        assertDoesNotThrow(
                () -> applicationService.validateTransition(ApplicationStatus.SUBMITTED, ApplicationStatus.REJECTED));
    }

    @Test
    void validateTransition_SubmittedToInProgress() {
        assertDoesNotThrow(() -> applicationService.validateTransition(ApplicationStatus.SUBMITTED,
                ApplicationStatus.IN_PROGRESS));
    }

    @Test
    void validateTransition_AwardedToRejected() {
        assertDoesNotThrow(
                () -> applicationService.validateTransition(ApplicationStatus.AWARDED, ApplicationStatus.REJECTED));
    }

    @Test
    void validateTransition_RejectedToSaved() {
        assertDoesNotThrow(
                () -> applicationService.validateTransition(ApplicationStatus.REJECTED, ApplicationStatus.SAVED));
    }

    @Test
    void validateTransition_RejectedToInProgress() {
        assertDoesNotThrow(
                () -> applicationService.validateTransition(ApplicationStatus.REJECTED, ApplicationStatus.IN_PROGRESS));
    }

    @Test
    void validateTransition_ThrowsOnInvalidTransitionFromAwarded() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> applicationService.validateTransition(ApplicationStatus.AWARDED, ApplicationStatus.SAVED));
        assertTrue(ex.getMessage().contains("Invalid transition"));
    }

    @Test
    void validateTransition_ThrowsOnInvalidTransitionFromRejected() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> applicationService.validateTransition(ApplicationStatus.REJECTED, ApplicationStatus.SUBMITTED));
        assertTrue(ex.getMessage().contains("Invalid transition"));
    }

    @Test
    void validateTransition_ThrowsOnInvalidTransitionFromSubmitted() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> applicationService.validateTransition(ApplicationStatus.SUBMITTED, ApplicationStatus.SAVED));
        assertTrue(ex.getMessage().contains("Invalid transition"));
    }

    // --- getApplications ---

    @Test
    void getApplications_ReturnsListForUser() {
        when(applicationRepository.findByUser_UserId(1L)).thenReturn(List.of(testApplication));

        List<Application> result = applicationService.getApplications(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getApplications_ReturnsEmptyListWhenNoneFound() {
        when(applicationRepository.findByUser_UserId(99L)).thenReturn(List.of());

        List<Application> result = applicationService.getApplications(99L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // --- deleteApplication ---

    @Test
    void deleteApplication_CallsRepositoryDelete() {
        doNothing().when(applicationRepository).deleteById(1L);

        applicationService.deleteApplication(1L);

        verify(applicationRepository, times(1)).deleteById(1L);
    }
}