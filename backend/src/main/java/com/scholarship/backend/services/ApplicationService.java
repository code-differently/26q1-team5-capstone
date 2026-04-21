package com.scholarship.backend.services;

import com.scholarship.backend.entities.*;
import com.scholarship.backend.repositories.ApplicationRepository;
import com.scholarship.backend.repositories.ScholarshipRepository;
import com.scholarship.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final ScholarshipRepository scholarshipRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository,
            UserRepository userRepository,
            ScholarshipRepository scholarshipRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.scholarshipRepository = scholarshipRepository;
    }

    public Application createApplication(long userId, long scholarshipId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        Scholarship scholarship = scholarshipRepository.findById(scholarshipId).orElse(null);
        if (scholarship == null) {
            throw new IllegalArgumentException("Scholarship not found with ID: " + scholarshipId);
        }

        List<Application> existingApplications = applicationRepository.findByUser_UserId(userId);
        boolean alreadyApplied = existingApplications.stream()
                .anyMatch(app -> app.getScholarship().getScholarshipId() == scholarshipId);

        if (alreadyApplied) {
            throw new IllegalArgumentException("You already have an application for this scholarship");
        }

        Application application = new Application(user, scholarship);
        return applicationRepository.save(application);
    }

    public Application updateStatus(long applicationId, ApplicationStatus newStatus) {
        Application application = applicationRepository.findById(applicationId).orElse(null);
        if (application == null) {
            throw new IllegalArgumentException("Application not found with ID: " + applicationId);
        }

        validateTransition(application.getStatus(), newStatus);

        application.setStatus(newStatus);

        if (newStatus == ApplicationStatus.SUBMITTED) {
            application.setSubmittedDate(LocalDate.now());
        }

        return applicationRepository.save(application);
    }

    public void validateTransition(ApplicationStatus from, ApplicationStatus to) {
        if (from == to)
            return;

        switch (from) {
            case SAVED:
                if (to != ApplicationStatus.IN_PROGRESS &&
                        to != ApplicationStatus.SUBMITTED &&
                        to != ApplicationStatus.AWARDED &&
                        to != ApplicationStatus.REJECTED) {
                    throw new IllegalArgumentException("Invalid transition from " + from + " to " + to);
                }
                break;
            case IN_PROGRESS:
                if (to != ApplicationStatus.SAVED &&
                        to != ApplicationStatus.SUBMITTED &&
                        to != ApplicationStatus.AWARDED &&
                        to != ApplicationStatus.REJECTED) {
                    throw new IllegalArgumentException("Invalid transition from " + from + " to " + to);
                }
                break;
            case SUBMITTED:
                if (to != ApplicationStatus.AWARDED &&
                        to != ApplicationStatus.REJECTED &&
                        to != ApplicationStatus.IN_PROGRESS) {
                    throw new IllegalArgumentException("Invalid transition from " + from + " to " + to);
                }
                break;
            case AWARDED:
                if (to != ApplicationStatus.REJECTED) {
                    throw new IllegalArgumentException("Invalid transition from " + from + " to " + to);
                }
                break;
            case REJECTED:
                if (to != ApplicationStatus.SAVED &&
                        to != ApplicationStatus.IN_PROGRESS) {
                    throw new IllegalArgumentException("Invalid transition from " + from + " to " + to);
                }
                break;
        }
    }

    public List<Application> getApplications(long userId) {
        return applicationRepository.findByUser_UserId(userId);
    }

    public void deleteApplication(long applicationId) {
        applicationRepository.deleteById(applicationId);
    }
}