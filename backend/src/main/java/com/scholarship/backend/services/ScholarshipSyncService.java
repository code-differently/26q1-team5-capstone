package com.scholarship.backend.services;

import com.scholarship.backend.entities.Scholarship;
import com.scholarship.backend.repositories.ScholarshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScholarshipSyncService {

    private final ScholarshipRepository scholarshipRepository;
    private final ExternalApiClient externalApiClient;

    @Autowired
    public ScholarshipSyncService(ScholarshipRepository scholarshipRepository, ExternalApiClient externalApiClient) {
        this.scholarshipRepository = scholarshipRepository;
        this.externalApiClient = externalApiClient;
    }

    public List<Scholarship> syncFromExternalApi() {
        // Fetch scholarships from external API
        List<Scholarship> externalScholarships = externalApiClient.fetchScholarshipsFromApi();

        // Save them to the database
        List<Scholarship> savedScholarships = scholarshipRepository.saveAll(externalScholarships);

        return savedScholarships;
    }

    public void refreshScholarships() {
        // This could implement logic to update existing scholarships
        // For now, just sync from external API
        syncFromExternalApi();
    }
}
