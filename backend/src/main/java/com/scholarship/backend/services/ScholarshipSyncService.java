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
            // Deduplicate by name + sourceApi — avoids duplicate rows on repeated syncs
            boolean alreadyExists = scholarshipRepository
                .existsByNameAndSourceApi(scholarship.getName(), scholarship.getSourceApi());

            if (!alreadyExists) {
                toSave.add(scholarship);
            }
        }

        return scholarshipRepository.saveAll(toSave);
    }

    public void refreshScholarships() {
        syncFromExternalApi();
    }
}