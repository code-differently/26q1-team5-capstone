package com.scholarship.backend.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "scholarships")
public class Scholarship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long scholarshipId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private Double amount;

    @Column
    private LocalDate deadline;

    @Column(columnDefinition = "TEXT")
    private String eligibilityCriteria;

    @Column
    private String applicationUrl;

    @Column
    private String fieldOfStudy;

    @Column
    private String state;

    @Column
    private String sourceApi;

    @OneToMany(mappedBy = "scholarship", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();

    public Scholarship() {}

    public Scholarship(String name, Double amount, LocalDate deadline) {
        this.name = name;
        this.amount = amount;
        this.deadline = deadline;
    }

    // Getters
    public long getScholarshipId() {
        return scholarshipId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public String getEligibilityCriteria() {
        return eligibilityCriteria;
    }

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public String getState() {
        return state;
    }

    public String getSourceApi() {
        return sourceApi;
    }

    public List<Application> getApplications() {
        return applications;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setEligibilityCriteria(String eligibilityCriteria) {
        this.eligibilityCriteria = eligibilityCriteria;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setSourceApi(String sourceApi) {
        this.sourceApi = sourceApi;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
}
