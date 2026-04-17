package com.scholarship.backend.controllers;

import com.scholarship.backend.services.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")

public class MatchController {

    @Autowired
    private MatchingService matchingService;

    @GetMapping("/{userId}/ai-search")
    public ResponseEntity<String> getAIScholarshipSearch(@PathVariable Long userId) {
        String aiResponse = matchingService.getAIScholarshipSearch(userId);
        return ResponseEntity.ok(aiResponse);
    }
}