package com.scholarship.backend.services;

import com.scholarship.backend.entities.Scholarship;
import com.scholarship.backend.repositories.ScholarshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<Scholarship> incoming = externalApiClient.fetchScholarshipsFromApi();
        List<Scholarship> toSave = new ArrayList<>();

        for (Scholarship scholarship : incoming) {
            // Check if scholarship already exists
            Scholarship existing = scholarshipRepository
                .findByNameAndSourceApi(scholarship.getName(), scholarship.getSourceApi());

            if (existing != null) {
                // Update existing scholarship with new information (especially amount)
                existing.setAmount(scholarship.getAmount());
                existing.setDescription(scholarship.getDescription());
                existing.setDeadline(scholarship.getDeadline());
                existing.setEligibilityCriteria(scholarship.getEligibilityCriteria());
                existing.setApplicationUrl(scholarship.getApplicationUrl());
                existing.setFieldOfStudy(scholarship.getFieldOfStudy());
                existing.setState(scholarship.getState());
                toSave.add(existing);
            } else {
                toSave.add(scholarship);
            }
        }

        return scholarshipRepository.saveAll(toSave);
    }

    public void refreshScholarships() {
        syncFromExternalApi();
    }

    public void syncIfNeeded() {
    if (scholarshipRepository.count() == 0) {
        syncFromExternalApi();
    }
}
}