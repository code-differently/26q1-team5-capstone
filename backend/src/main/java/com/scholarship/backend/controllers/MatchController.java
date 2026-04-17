package com.scholarship.backend.controllers;

import com.scholarship.backend.services.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://127.0.0.1:5173",
    "http://localhost:5174",
    "http://127.0.0.1:5174"
})
public class MatchController {

    @Autowired
    private MatchingService matchingService;

    @GetMapping("/{userId}/ai-search")
    public ResponseEntity<String> getAIScholarshipSearch(@PathVariable Long userId) {
        String aiResponse = matchingService.getAIScholarshipSearch(userId);
        return ResponseEntity.ok(aiResponse);
    }
}