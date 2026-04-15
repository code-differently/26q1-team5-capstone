package com.scholarship.backend.services;

import com.scholarship.backend.entities.Profile;
import com.scholarship.backend.entities.Scholarship;
import com.scholarship.backend.repositories.ProfileRepository;
import com.scholarship.backend.repositories.ScholarshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        // Get user profile
        Profile profile = profileRepository.findByUser_UserId(userId);
        if (profile == null) {
            throw new IllegalArgumentException("Profile not found for user: " + userId);
        }

        // Get all available scholarships
        List<Scholarship> allScholarships = scholarshipRepository.findAll();

        // Use AI to get personalized matches
        String prompt = promptBuilder.buildPrompt(profile, allScholarships);
        String aiResponse = aiClient.getCompletion(prompt);

        // Parse AI response and return matched scholarships
        List<Scholarship> matches = promptBuilder.parseResponse(aiResponse, allScholarships);

        // Rank by deadline priority
        return rankByDeadlinePriority(matches);
    }

    public void refreshMatchesForUser(long userId) {
        // This could implement caching or updating match recommendations
        // For now, just ensure the user has a profile
        Profile profile = profileRepository.findByUser_UserId(userId);
        if (profile == null) {
            throw new IllegalArgumentException("Profile not found for user: " + userId);
        }
        // In a real implementation, this might clear caches or update recommendations
    }

    public List<Scholarship> rankByDeadlinePriority(List<Scholarship> matches) {
        LocalDate now = LocalDate.now();

        return matches.stream()
                .filter(scholarship -> scholarship.getDeadline() != null)
                .sorted(Comparator.comparing(Scholarship::getDeadline))
                .collect(Collectors.toList());
    }
}
