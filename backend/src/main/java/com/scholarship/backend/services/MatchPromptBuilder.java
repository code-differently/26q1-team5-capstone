package com.scholarship.backend.services;

import com.scholarship.backend.entities.Profile;
import org.springframework.stereotype.Component;

@Component
public class MatchPromptBuilder {

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
        prompt.append("- Do NOT return JSON\n");
        prompt.append("- Do NOT use markdown\n");
        prompt.append("- Keep it clean and readable\n");
        prompt.append("- Focus on strong matches based on GPA, major, and goals\n");

        return prompt.toString();
    }
}