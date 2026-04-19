package com.scholarship.backend.services;

import com.scholarship.backend.entities.Scholarship;
import com.scholarship.backend.repositories.ScholarshipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScholarshipSyncServiceTest {

    @Mock
    private ScholarshipRepository scholarshipRepository;

    @Mock
    private ExternalApiClient externalApiClient;

    @InjectMocks
    private ScholarshipSyncService scholarshipSyncService;

    private Scholarship incomingScholarship;
    private Scholarship existingScholarship;

    @BeforeEach
    void setUp() {
        incomingScholarship = new Scholarship();
        incomingScholarship.setName("NSF STEM Scholarship");
        incomingScholarship.setDescription("Updated description");
        incomingScholarship.setAmount(6000.0);
        incomingScholarship.setDeadline(LocalDate.of(2027, 6, 1));
        incomingScholarship.setEligibilityCriteria("GPA 3.0+");
        incomingScholarship.setApplicationUrl("https://nsf.gov");
        incomingScholarship.setFieldOfStudy("Engineering");
        incomingScholarship.setState("National");
        incomingScholarship.setSourceApi("GRANTS_GOV");

        existingScholarship = new Scholarship();
        existingScholarship.setName("NSF STEM Scholarship");
        existingScholarship.setDescription("Old description");
        existingScholarship.setAmount(5000.0);
        existingScholarship.setDeadline(LocalDate.of(2026, 1, 1));
        existingScholarship.setSourceApi("GRANTS_GOV");
    }

    // --- syncFromExternalApi ---

    @Test
    void syncFromExternalApi_SavesNewScholarshipWhenNotExists() {
        when(externalApiClient.fetchScholarshipsFromApi()).thenReturn(List.of(incomingScholarship));
        when(scholarshipRepository.findByNameAndSourceApi(
            incomingScholarship.getName(), incomingScholarship.getSourceApi()
        )).thenReturn(null);
        when(scholarshipRepository.saveAll(anyList())).thenReturn(List.of(incomingScholarship));

        List<Scholarship> result = scholarshipSyncService.syncFromExternalApi();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(scholarshipRepository, times(1)).saveAll(anyList());
    }

    @Test
    void syncFromExternalApi_UpdatesExistingScholarship() {
        when(externalApiClient.fetchScholarshipsFromApi()).thenReturn(List.of(incomingScholarship));
        when(scholarshipRepository.findByNameAndSourceApi(
            incomingScholarship.getName(), incomingScholarship.getSourceApi()
        )).thenReturn(existingScholarship);
        when(scholarshipRepository.saveAll(anyList())).thenReturn(List.of(existingScholarship));

        scholarshipSyncService.syncFromExternalApi();

        // Verify existing scholarship was updated with incoming data
        assertEquals(6000.0, existingScholarship.getAmount());
        assertEquals("Updated description", existingScholarship.getDescription());
        assertEquals(LocalDate.of(2027, 6, 1), existingScholarship.getDeadline());
        assertEquals("GPA 3.0+", existingScholarship.getEligibilityCriteria());
        assertEquals("https://nsf.gov", existingScholarship.getApplicationUrl());
        assertEquals("Engineering", existingScholarship.getFieldOfStudy());
        assertEquals("National", existingScholarship.getState());
        verify(scholarshipRepository, times(1)).saveAll(anyList());
    }

    @Test
    void syncFromExternalApi_HandlesEmptyApiResponse() {
        when(externalApiClient.fetchScholarshipsFromApi()).thenReturn(List.of());
        when(scholarshipRepository.saveAll(anyList())).thenReturn(List.of());

        List<Scholarship> result = scholarshipSyncService.syncFromExternalApi();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scholarshipRepository, times(1)).saveAll(anyList());
    }

    @Test
    void syncFromExternalApi_HandlesMixOfNewAndExisting() {
        Scholarship newScholarship = new Scholarship();
        newScholarship.setName("Brand New Scholarship");
        newScholarship.setSourceApi("GRANTS_GOV");

        when(externalApiClient.fetchScholarshipsFromApi())
            .thenReturn(List.of(incomingScholarship, newScholarship));
        when(scholarshipRepository.findByNameAndSourceApi(
            incomingScholarship.getName(), incomingScholarship.getSourceApi()
        )).thenReturn(existingScholarship);
        when(scholarshipRepository.findByNameAndSourceApi(
            newScholarship.getName(), newScholarship.getSourceApi()
        )).thenReturn(null);
        when(scholarshipRepository.saveAll(anyList()))
            .thenReturn(List.of(existingScholarship, newScholarship));

        List<Scholarship> result = scholarshipSyncService.syncFromExternalApi();

        assertEquals(2, result.size());
        verify(scholarshipRepository, times(1)).saveAll(anyList());
    }

    // --- refreshScholarships ---

    @Test
    void refreshScholarships_CallsSyncFromExternalApi() {
        when(externalApiClient.fetchScholarshipsFromApi()).thenReturn(List.of());
        when(scholarshipRepository.saveAll(anyList())).thenReturn(List.of());

        scholarshipSyncService.refreshScholarships();

        verify(externalApiClient, times(1)).fetchScholarshipsFromApi();
        verify(scholarshipRepository, times(1)).saveAll(anyList());
    }

    // --- syncIfNeeded ---

    @Test
    void syncIfNeeded_SyncsWhenRepositoryIsEmpty() {
        when(scholarshipRepository.count()).thenReturn(0L);
        when(externalApiClient.fetchScholarshipsFromApi()).thenReturn(List.of());
        when(scholarshipRepository.saveAll(anyList())).thenReturn(List.of());

        scholarshipSyncService.syncIfNeeded();

        verify(externalApiClient, times(1)).fetchScholarshipsFromApi();
    }

    @Test
    void syncIfNeeded_DoesNotSyncWhenRepositoryHasData() {
        when(scholarshipRepository.count()).thenReturn(5L);

        scholarshipSyncService.syncIfNeeded();

        verify(externalApiClient, never()).fetchScholarshipsFromApi();
        verify(scholarshipRepository, never()).saveAll(anyList());
    }
}