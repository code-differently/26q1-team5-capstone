package com.scholarship.backend.services;

import com.scholarship.backend.entities.Profile;
import com.scholarship.backend.repositories.ProfileRepository;
import com.scholarship.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public Profile createProfile(Profile profile) {
        Profile existingProfile = profileRepository.findByUser_UserId(profile.getUser().getUserId());
        if (existingProfile != null) {
            throw new IllegalArgumentException("Profile already exists for user: " + profile.getUser().getUserId());
        }
        return profileRepository.save(profile);
    }

    public Profile getProfile(long userId) {
        return profileRepository.findByUser_UserId(userId);
    }

    public Profile updateProfile(long profileId, Profile updatedProfile) {
        Profile existingProfile = profileRepository.findById(profileId).orElse(null);
        if (existingProfile == null) {
            throw new IllegalArgumentException("Profile not found with ID: " + profileId);
        }

        existingProfile.setName(updatedProfile.getName());
        existingProfile.setGpa(updatedProfile.getGpa());
        existingProfile.setMajor(updatedProfile.getMajor());
        existingProfile.setEnrollmentStatus(updatedProfile.getEnrollmentStatus());
        existingProfile.setNeedsFinancialAid(updatedProfile.getNeedsFinancialAid());
        existingProfile.setState(updatedProfile.getState());
        existingProfile.setEthnicity(updatedProfile.getEthnicity());
        existingProfile.setCareerGoals(updatedProfile.getCareerGoals());
        existingProfile.setInterests(updatedProfile.getInterests());

        return profileRepository.save(existingProfile);
    }

    public void deleteProfile(long userId) {
        Profile existingProfile = profileRepository.findByUser_UserId(userId);
        if (existingProfile == null) {
            throw new IllegalArgumentException("Profile not found for user: " + userId);
        }
        userRepository.deleteById(userId);
    }
}