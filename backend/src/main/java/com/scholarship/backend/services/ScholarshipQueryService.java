package com.scholarship.backend.services;

import com.scholarship.backend.entities.Scholarship;
import com.scholarship.backend.repositories.ScholarshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScholarshipQueryService {

    private final ScholarshipRepository scholarshipRepository;

    @Autowired
    public ScholarshipQueryService(ScholarshipRepository scholarshipRepository) {
        this.scholarshipRepository = scholarshipRepository;
    }

    public Scholarship getScholarshipById(long id) {
        return scholarshipRepository.findById(id).orElse(null);
    }

    public List<Scholarship> searchScholarships(String query) {
        if (query == null || query.trim().isEmpty()) {
            return scholarshipRepository.findAll();
        }

        String lowerQuery = query.toLowerCase().trim();

        return scholarshipRepository.findAll().stream()
                .filter(scholarship ->
                    scholarship.getName() != null && scholarship.getName().toLowerCase().contains(lowerQuery) ||
                    scholarship.getDescription() != null && scholarship.getDescription().toLowerCase().contains(lowerQuery) ||
                    scholarship.getFieldOfStudy() != null && scholarship.getFieldOfStudy().toLowerCase().contains(lowerQuery) ||
                    scholarship.getState() != null && scholarship.getState().toLowerCase().contains(lowerQuery) ||
                    scholarship.getEligibilityCriteria() != null && scholarship.getEligibilityCriteria().toLowerCase().contains(lowerQuery)
                )
                .collect(Collectors.toList());
    }

    public List<Scholarship> filterByField(String field) {
        return scholarshipRepository.findByFieldOfStudy(field);
    }

    public List<Scholarship> filterByState(String state) {
        return scholarshipRepository.findByState(state);
    }
}
