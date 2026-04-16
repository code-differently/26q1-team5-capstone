package com.scholarship.backend.services;

import com.scholarship.backend.entities.Profile;
import com.scholarship.backend.entities.Scholarship;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MatchPromptBuilder {

    public String buildPrompt(Profile profile, List<Scholarship> scholarships) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Based on the following student profile, recommend the most suitable scholarships from the provided list.\n\n");

        // Add student profile information
        prompt.append("STUDENT PROFILE:\n");
        prompt.append("Name: ").append(profile.getName()).append("\n");
        if (profile.getGpa() != null) {
            prompt.append("GPA: ").append(profile.getGpa()).append("\n");
        }
        if (profile.getMajor() != null) {
            prompt.append("Major: ").append(profile.getMajor()).append("\n");
        }
        if (profile.getEnrollmentStatus() != null) {
            prompt.append("Enrollment Status: ").append(profile.getEnrollmentStatus()).append("\n");
        }
        if (profile.getNeedsFinancialAid() != null) {
            prompt.append("Needs Financial Aid: ").append(profile.getNeedsFinancialAid()).append("\n");
        }
        if (profile.getState() != null) {
            prompt.append("State: ").append(profile.getState()).append("\n");
        }
        if (profile.getEthnicity() != null) {
            prompt.append("Ethnicity: ").append(profile.getEthnicity()).append("\n");
        }
        if (profile.getCareerGoals() != null) {
            prompt.append("Career Goals: ").append(profile.getCareerGoals()).append("\n");
        }
        if (profile.getInterests() != null) {
            prompt.append("Interests: ").append(profile.getInterests()).append("\n");
        }

        // Add available scholarships
        prompt.append("\nAVAILABLE SCHOLARSHIPS:\n");
        for (int i = 0; i < scholarships.size(); i++) {
            Scholarship scholarship = scholarships.get(i);
            prompt.append(i + 1).append(". ");
            prompt.append(scholarship.getName()).append(" - ");
            if (scholarship.getAmount() != null) {
                prompt.append("$").append(scholarship.getAmount());
            }
            if (scholarship.getFieldOfStudy() != null) {
                prompt.append(" (").append(scholarship.getFieldOfStudy()).append(")");
            }
            if (scholarship.getState() != null) {
                prompt.append(" - ").append(scholarship.getState());
            }
            prompt.append("\n");
            if (scholarship.getDescription() != null) {
                prompt.append("   Description: ").append(scholarship.getDescription()).append("\n");
            }
            if (scholarship.getEligibilityCriteria() != null) {
                prompt.append("   Eligibility: ").append(scholarship.getEligibilityCriteria()).append("\n");
            }
        }

        prompt.append("\nPlease return a ranked list of the top 5 most suitable scholarships for this student, considering their profile, needs, and eligibility. Return only the scholarship IDs in order of preference, separated by commas.");

        return prompt.toString();
    }

    public List<Scholarship> parseResponse(String response, List<Scholarship> allScholarships) {
        // This is a simplified implementation
        // In a real AI integration, the response would be parsed properly
        // For now, return the first few scholarships as matches

        return allScholarships.stream().limit(5).toList();
    }
}
