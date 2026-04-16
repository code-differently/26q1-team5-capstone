package com.scholarship.backend.controllers;

import com.scholarship.backend.entities.Scholarship;
import com.scholarship.backend.services.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
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
}
