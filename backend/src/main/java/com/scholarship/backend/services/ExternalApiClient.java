package com.scholarship.backend.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.scholarship.backend.entities.Scholarship;
import org.springframework.stereotype.Component;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ExternalApiClient {

    private static final String GRANTS_GOV_URL = "https://api.grants.gov/v1/api/search2";
    private static final DateTimeFormatter GRANTS_DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private final RestTemplate restTemplate;

    public ExternalApiClient() {
        this.restTemplate = new RestTemplate();
    }

    public List<Scholarship> fetchScholarshipsFromApi() {
        // Build the request body
        Map<String, Object> requestBody = Map.of(
            "keyword", "scholarship student",
            "oppStatuses", "posted",
            "rows", 50
        );

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        // Make the POST call
        ResponseEntity<GrantsGovResponse> response = restTemplate.exchange(
            GRANTS_GOV_URL,
            HttpMethod.POST,
            request,
            GrantsGovResponse.class
        );

        // Map results to your Scholarship entity
        List<Scholarship> scholarships = new ArrayList<>();

        if (response.getBody() != null
                && response.getBody().getData() != null
                && response.getBody().getData().getOppHits() != null) {

            for (OppHit hit : response.getBody().getData().getOppHits()) {
                Scholarship scholarship = mapToScholarship(hit);
                scholarships.add(scholarship);
            }
        }

        return scholarships;
    }

    private Scholarship mapToScholarship(OppHit hit) {
        Scholarship scholarship = new Scholarship();

        scholarship.setName(hit.getTitle() != null ? hit.getTitle() : "Untitled");
        scholarship.setDescription(hit.getAgencyName() != null ? "Agency: " + hit.getAgencyName() : "Scholarship opportunity");
        scholarship.setApplicationUrl("https://grants.gov/search-results-detail/" + hit.getId());
        scholarship.setSourceApi("GRANTS_GOV");

        scholarship.setAmount(generateDefaultAmount(hit));

        // Parse closeDate safely
        if (hit.getCloseDate() != null && !hit.getCloseDate().isBlank()) {
            try {
                scholarship.setDeadline(LocalDate.parse(hit.getCloseDate(), GRANTS_DATE_FORMAT));
            } catch (DateTimeParseException e) {
                scholarship.setDeadline(null);
            }
        }

        return scholarship;
    }

    private Double generateDefaultAmount(OppHit hit) {
        // Generate reasonable default amounts based on keywords in title or agency
        String title = hit.getTitle() != null ? hit.getTitle().toLowerCase() : "";
        String agency = hit.getAgencyName() != null ? hit.getAgencyName().toLowerCase() : "";

        if (title.contains("fellowship") || title.contains("phd") || title.contains("doctoral")) {
            return 30000.0; // Higher amounts for fellowships
        } else if (title.contains("scholarship") || title.contains("student")) {
            return 5000.0; // Standard scholarship amount
        } else if (title.contains("research") || title.contains("grant")) {
            return 25000.0; // Research grants
        } else if (agency.contains("education") || agency.contains("department of education")) {
            return 4000.0; // Education-focused
        } else if (agency.contains("science") || agency.contains("nsf") || agency.contains("foundation")) {
            return 15000.0; // Science/research focused
        } else if (agency.contains("defense") || agency.contains("army") || agency.contains("navy")) {
            return 10000.0; // Defense-related
        } else {
            // Default amount for other opportunities
            return 7500.0;
        }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    static class GrantsGovResponse {
        @JsonProperty("data")
        private GrantsGovData data;

        public GrantsGovData getData() { return data; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class GrantsGovData {
        @JsonProperty("oppHits")
        private List<OppHit> oppHits;

        public List<OppHit> getOppHits() { return oppHits; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class OppHit {
        @JsonProperty("id")
        private String id;

        @JsonProperty("title")
        private String title;

        @JsonProperty("agencyName")
        private String agencyName;

        @JsonProperty("openDate")
        private String openDate;

        @JsonProperty("closeDate")
        private String closeDate;

        @JsonProperty("oppStatus")
        private String oppStatus;

        public String getId()         { return id; }
        public String getTitle()      { return title; }
        public String getAgencyName() { return agencyName; }
        public String getOpenDate()   { return openDate; }
        public String getCloseDate()  { return closeDate; }
        public String getOppStatus()  { return oppStatus; }
    }
}