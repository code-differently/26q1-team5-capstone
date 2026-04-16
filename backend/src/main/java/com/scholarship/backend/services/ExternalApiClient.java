package com.scholarship.backend.services;

import com.scholarship.backend.entities.Scholarship;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class ExternalApiClient {

    private final RestTemplate restTemplate;

    public ExternalApiClient() {
        this.restTemplate = new RestTemplate();
    }

    public List<Scholarship> fetchScholarshipsFromApi() {
        // This is a mock implementation
        // In a real application, this would call an external API
        // For now, return some sample scholarships

        Scholarship scholarship1 = new Scholarship("External Scholarship 1", 2500.0, LocalDate.of(2026, 8, 15));
        scholarship1.setDescription("Award for academic excellence");
        scholarship1.setFieldOfStudy("Computer Science");
        scholarship1.setState("California");
        scholarship1.setSourceApi("ExternalAPI");

        Scholarship scholarship2 = new Scholarship("External Scholarship 2", 1500.0, LocalDate.of(2026, 9, 30));
        scholarship2.setDescription("STEM scholarship for underrepresented groups");
        scholarship2.setFieldOfStudy("Engineering");
        scholarship2.setState("Texas");
        scholarship2.setSourceApi("ExternalAPI");

        return Arrays.asList(scholarship1, scholarship2);
    }
}
