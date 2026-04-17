package com.scholarship.backend.controllers;

import com.scholarship.backend.entities.Scholarship;
import com.scholarship.backend.services.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = { "http://localhost:5173", "http://127.0.0.1:5173", "http://localhost:5174",
        "http://127.0.0.1:5174" })
public class MatchController {

    @Autowired
    private MatchingService matchingService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Scholarship>> getMatches(@PathVariable Long userId) {
        List<Scholarship> matches = matchingService.getMatchesForUser(userId);
        return ResponseEntity.ok(matches);
    }

    @PostMapping("/{userId}/refresh")
    public ResponseEntity<Void> refreshMatches(@PathVariable Long userId) {
        matchingService.refreshMatchesForUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    public ResponseEntity<String> testAI() {
        String result = matchingService.testAI();
        return ResponseEntity.ok(result);
    }
}
