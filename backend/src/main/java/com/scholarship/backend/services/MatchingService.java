package com.scholarship.backend.services;

import com.scholarship.backend.entities.Profile;
import com.scholarship.backend.entities.Scholarship;
import com.scholarship.backend.repositories.ProfileRepository;
import com.scholarship.backend.repositories.ScholarshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchingService {

    private final ProfileRepository profileRepository;
    private final ScholarshipRepository scholarshipRepository;
    private final MatchPromptBuilder promptBuilder;
    private final AIClient aiClient;

    @Autowired
    public MatchingService(ProfileRepository profileRepository,
            ScholarshipRepository scholarshipRepository,
            MatchPromptBuilder promptBuilder,
            AIClient aiClient) {
        this.profileRepository = profileRepository;
        this.scholarshipRepository = scholarshipRepository;
        this.promptBuilder = promptBuilder;
        this.aiClient = aiClient;
    }

    public List<Scholarship> getMatchesForUser(long userId) {
        Profile profile = profileRepository.findByUser_UserId(userId);
        if (profile == null) {
            throw new IllegalArgumentException("Profile not found for user: " + userId);
        }

        // AI finds scholarships based on profile — no DB lookup needed as input
        String prompt = promptBuilder.buildPrompt(profile);
        String aiResponse = aiClient.getCompletion(prompt);

        // Parse into Scholarship entities
        List<Scholarship> aiScholarships = promptBuilder.parseResponse(aiResponse);

        // Deduplicate and save new ones to DB
        List<Scholarship> toSave = new ArrayList<>();
        for (Scholarship s : aiScholarships) {
            boolean exists = scholarshipRepository
                .existsByNameAndSourceApi(s.getName(), "CLAUDE_AI");
            if (!exists) {
                toSave.add(s);
            }
        }
        if (!toSave.isEmpty()) {
            scholarshipRepository.saveAll(toSave);
        }

        // Return all AI-sourced scholarships ranked by deadline
        return rankByDeadlinePriority(
            scholarshipRepository.findBySourceApi("CLAUDE_AI")
        );
    }

    public void refreshMatchesForUser(long userId) {
        Profile profile = profileRepository.findByUser_UserId(userId);
        if (profile == null) {
            throw new IllegalArgumentException("Profile not found for user: " + userId);
        }

        // Wipe existing AI scholarships and re-search
        List<Scholarship> existing = scholarshipRepository.findBySourceApi("CLAUDE_AI");
        scholarshipRepository.deleteAll(existing);

        getMatchesForUser(userId);
    }

    public List<Scholarship> rankByDeadlinePriority(List<Scholarship> matches) {
        LocalDate now = LocalDate.now();
        return matches.stream()
            .filter(s -> s.getDeadline() != null && s.getDeadline().isAfter(now))
            .sorted(Comparator.comparing(Scholarship::getDeadline))
            .collect(Collectors.toList());
    }

    // Keep your test method — useful for verifying the API connection works
    public String testAI() {
        String prompt = """
                Student:
                - GPA: 3.5
                - Major: Computer Science

                Return ONLY a JSON array of 2 scholarships you know exist for CS students:
                [
                  {
                    "name": "...",
                    "description": "...",
                    "amount": 5000,
                    "deadline": "2025-12-31",
                    "eligibilityCriteria": "...",
                    "applicationUrl": "https://...",
                    "fieldOfStudy": "Computer Science",
                    "state": "National"
                  }
                ]
                """;
        return aiClient.getCompletion(prompt);
    }
}