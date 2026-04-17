package com.scholarship.backend.controllers;

import com.scholarship.backend.entities.Profile;
import com.scholarship.backend.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping
    public ResponseEntity<Profile> createProfile(@RequestBody Profile profile) {
        Profile createdProfile = profileService.createProfile(profile);
        return ResponseEntity.ok(createdProfile);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Profile> getProfile(@PathVariable Long userId) {
        Profile profile = profileService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{profileId}")
    public ResponseEntity<Profile> updateProfile(@PathVariable Long profileId, @RequestBody Profile profile) {
        Profile updatedProfile = profileService.updateProfile(profileId, profile);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long userId) {
        try {
            profileService.deleteProfile(userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            System.err.println("Error deleting profile: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
