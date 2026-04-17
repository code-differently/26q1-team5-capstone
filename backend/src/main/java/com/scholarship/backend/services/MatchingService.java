package com.scholarship.backend.services;

import com.scholarship.backend.entities.Profile;
import com.scholarship.backend.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MatchingService {

    private final ProfileRepository profileRepository;
    private final MatchPromptBuilder promptBuilder;
    private final AIClient aiClient;

    @Autowired
    public MatchingService(ProfileRepository profileRepository,
                           MatchPromptBuilder promptBuilder,
                           AIClient aiClient) {
        this.profileRepository = profileRepository;
        this.promptBuilder = promptBuilder;
        this.aiClient = aiClient;
    }

    public String getAIScholarshipSearch(long userId) {
        Profile profile = profileRepository.findByUser_UserId(userId);

        if (profile == null) {
            throw new IllegalArgumentException("Profile not found for user: " + userId);
        }

        String prompt = promptBuilder.buildPrompt(profile);
        return aiClient.getCompletion(prompt);
    }
}
