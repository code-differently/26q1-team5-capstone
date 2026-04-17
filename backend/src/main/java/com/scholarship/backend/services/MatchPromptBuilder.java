package com.scholarship.backend.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scholarship.backend.entities.Profile;
import com.scholarship.backend.entities.Scholarship;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MatchPromptBuilder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String buildPrompt(Profile profile) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are a scholarship search assistant. Search the internet right now for real, currently active scholarships that match the following student profile. Do not make up scholarships — only return ones you can verify exist online.\n\n");

        prompt.append("STUDENT PROFILE:\n");
        if (profile.getName() != null)
            prompt.append("Name: ").append(profile.getName()).append("\n");
        if (profile.getGpa() != null)
            prompt.append("GPA: ").append(profile.getGpa()).append("\n");
        if (profile.getMajor() != null)
            prompt.append("Major: ").append(profile.getMajor()).append("\n");
        if (profile.getEnrollmentStatus() != null)
            prompt.append("Enrollment Status: ").append(profile.getEnrollmentStatus()).append("\n");
        if (profile.getNeedsFinancialAid() != null)
            prompt.append("Needs Financial Aid: ").append(profile.getNeedsFinancialAid()).append("\n");
        if (profile.getState() != null)
            prompt.append("State: ").append(profile.getState()).append("\n");
        if (profile.getEthnicity() != null)
            prompt.append("Ethnicity: ").append(profile.getEthnicity()).append("\n");
        if (profile.getCareerGoals() != null)
            prompt.append("Career Goals: ").append(profile.getCareerGoals()).append("\n");
        if (profile.getInterests() != null)
            prompt.append("Interests: ").append(profile.getInterests()).append("\n");

        prompt.append("\nSearch the web for real scholarships that match this student. Return ONLY a JSON array with no extra text, markdown, or explanation. Each object must follow this exact format:\n");
        prompt.append("[\n");
        prompt.append("  {\n");
        prompt.append("    \"name\": \"Scholarship name\",\n");
        prompt.append("    \"description\": \"Brief description of the scholarship\",\n");
        prompt.append("    \"amount\": 5000.00,\n");
        prompt.append("    \"deadline\": \"2025-12-31\",\n");
        prompt.append("    \"eligibilityCriteria\": \"Who is eligible\",\n");
        prompt.append("    \"applicationUrl\": \"https://...\",\n");
        prompt.append("    \"fieldOfStudy\": \"Field or General\",\n");
        prompt.append("    \"state\": \"State or National\"\n");
        prompt.append("  }\n");
        prompt.append("]\n\n");
        prompt.append("Rules:\n");
        prompt.append("- Return between 5 and 10 scholarships\n");
        prompt.append("- deadline must be in YYYY-MM-DD format\n");
        prompt.append("- amount must be a number, not a string. If unknown use null\n");
        prompt.append("- applicationUrl must be a real working URL\n");
        prompt.append("- Do not wrap the response in markdown code blocks\n");
        prompt.append("- Return only the JSON array, nothing else\n");

        return prompt.toString();
    }

    public List<Scholarship> parseResponse(String response) {
        List<Scholarship> scholarships = new ArrayList<>();

        try {
            // Strip markdown code blocks if the model returns them anyway
            String cleaned = response.trim();
            if (cleaned.startsWith("```")) {
                cleaned = cleaned.replaceAll("```json", "").replaceAll("```", "").trim();
            }

            List<ScholarshipDTO> dtos = objectMapper.readValue(
                cleaned,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ScholarshipDTO.class)
            );

            for (ScholarshipDTO dto : dtos) {
                Scholarship scholarship = new Scholarship();
                scholarship.setName(dto.getName() != null ? dto.getName() : "Unnamed Scholarship");
                scholarship.setDescription(dto.getDescription());
                scholarship.setAmount(dto.getAmount());
                scholarship.setEligibilityCriteria(dto.getEligibilityCriteria());
                scholarship.setApplicationUrl(dto.getApplicationUrl());
                scholarship.setFieldOfStudy(dto.getFieldOfStudy());
                scholarship.setState(dto.getState());
                scholarship.setSourceApi("CLAUDE_AI");

                // Parse deadline safely
                if (dto.getDeadline() != null && !dto.getDeadline().isBlank()) {
                    try {
                        scholarship.setDeadline(LocalDate.parse(dto.getDeadline(),
                            DateTimeFormatter.ISO_LOCAL_DATE));
                    } catch (DateTimeParseException e) {
                        scholarship.setDeadline(null);
                    }
                }

                scholarships.add(scholarship);
            }

        } catch (Exception e) {
            System.err.println("Failed to parse Claude AI response: " + e.getMessage());
            System.err.println("Raw response was: " + response);
        }

        return scholarships;
    }

    // -------------------------
    // DTO for JSON deserialization
    // -------------------------

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class ScholarshipDTO {
        @JsonProperty("name")           private String name;
        @JsonProperty("description")    private String description;
        @JsonProperty("amount")         private Double amount;
        @JsonProperty("deadline")       private String deadline;
        @JsonProperty("eligibilityCriteria") private String eligibilityCriteria;
        @JsonProperty("applicationUrl") private String applicationUrl;
        @JsonProperty("fieldOfStudy")   private String fieldOfStudy;
        @JsonProperty("state")          private String state;

        public String getName()                 { return name; }
        public String getDescription()          { return description; }
        public Double getAmount()               { return amount; }
        public String getDeadline()             { return deadline; }
        public String getEligibilityCriteria()  { return eligibilityCriteria; }
        public String getApplicationUrl()       { return applicationUrl; }
        public String getFieldOfStudy()         { return fieldOfStudy; }
        public String getState()                { return state; }
    }
}