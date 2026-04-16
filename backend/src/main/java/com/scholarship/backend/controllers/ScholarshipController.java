package com.scholarship.backend.controllers;

import com.scholarship.backend.entities.Scholarship;
import com.scholarship.backend.services.ScholarshipQueryService;
import com.scholarship.backend.services.ScholarshipSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scholarships")
public class ScholarshipController {

    @Autowired
    private ScholarshipQueryService scholarshipQueryService;

    @Autowired
    private ScholarshipSyncService scholarshipSyncService;

    @GetMapping("/{id}")
    public ResponseEntity<Scholarship> getScholarshipById(@PathVariable Long id) {
        Scholarship scholarship = scholarshipQueryService.getScholarshipById(id);
        return ResponseEntity.ok(scholarship);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Scholarship>> searchScholarships(@RequestParam String query) {
        List<Scholarship> scholarships = scholarshipQueryService.searchScholarships(query);
        return ResponseEntity.ok(scholarships);
    }

    @PostMapping("/sync")
    public ResponseEntity<List<Scholarship>> syncFromApi() {
        List<Scholarship> scholarships = scholarshipSyncService.syncFromExternalApi();
        return ResponseEntity.ok(scholarships);
    }
}
