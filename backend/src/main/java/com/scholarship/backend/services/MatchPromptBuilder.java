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

    prompt.append("You are an AI scholarship recommendation assistant.\n\n");

    prompt.append("Based on the student profile below, suggest 5 scholarships ");
    prompt.append("that would be a strong match.\n");
    prompt.append("These should be realistic and similar to real scholarships, ");
    prompt.append("but they do not need to be verified or real-time.\n\n");

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

    prompt.append("\nReturn the results in this format:\n\n");

    prompt.append("1. Scholarship Name\n");
    prompt.append("- Amount: $5000\n");
    prompt.append("- Deadline: December 31, 2025\n");
    prompt.append("- Why it matches: Explain briefly based on the profile\n");
    prompt.append("- Apply: https://example.com\n\n");

    prompt.append("Rules:\n");
    prompt.append("- Do NOT use markdown\n");
    prompt.append("- Keep it clean and readable\n");
    prompt.append("- Focus on strong matches based on GPA, major, and goals\n");

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