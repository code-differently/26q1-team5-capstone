package com.scholarship.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "applications")
public class Application {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long applicationId;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "scholarship_id", nullable = false)
    @JsonIgnoreProperties({"applications"})
    private Scholarship scholarship;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;
    
    @Column
    private LocalDate savedDate;
    
    @Column
    private LocalDate submittedDate;
    
    @Column
    private LocalDate deadlineAlert;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    public Application() {}
    
    public Application(User user, Scholarship scholarship) {
        this.user = user;
        this.scholarship = scholarship;
        this.status = ApplicationStatus.SAVED;
        this.savedDate = LocalDate.now();
        this.deadlineAlert = scholarship.getDeadline();
    }
    
    // Getters
    public long getApplicationId() {
        return applicationId;
    }
    
    public User getUser() {
        return user;
    }
    
    public Scholarship getScholarship() {
        return scholarship;
    }
    
    public ApplicationStatus getStatus() {
        return status;
    }
    
    public LocalDate getSavedDate() {
        return savedDate;
    }
    
    public LocalDate getSubmittedDate() {
        return submittedDate;
    }
    
    public LocalDate getDeadlineAlert() {
        return deadlineAlert;
    }
    
    public String getNotes() {
        return notes;
    }
    
    // Setters
    public void setUser(User user) {
        this.user = user;
    }
    
    public void setScholarship(Scholarship scholarship) {
        this.scholarship = scholarship;
    }
    
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
    
    public void setSavedDate(LocalDate savedDate) {
        this.savedDate = savedDate;
    }
    
    public void setSubmittedDate(LocalDate submittedDate) {
        this.submittedDate = submittedDate;
    }
    
    public void setDeadlineAlert(LocalDate deadlineAlert) {
        this.deadlineAlert = deadlineAlert;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
