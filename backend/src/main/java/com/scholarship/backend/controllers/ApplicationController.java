package com.scholarship.backend.controllers;

import com.scholarship.backend.entities.Application;
import com.scholarship.backend.entities.ApplicationStatus;
import com.scholarship.backend.services.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "http://localhost:5174", "http://127.0.0.1:5174"})
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<Application> createApplication(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        Long scholarshipId = request.get("scholarshipId");
        Application application = applicationService.createApplication(userId, scholarshipId);
        return ResponseEntity.ok(application);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Application> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        ApplicationStatus status = ApplicationStatus.valueOf(request.get("status"));
        Application application = applicationService.updateStatus(id, status);
        return ResponseEntity.ok(application);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Application>> getApplications(@PathVariable Long userId) {
        List<Application> applications = applicationService.getApplications(userId);
        return ResponseEntity.ok(applications);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}
